package broccolai.tickets.core.commands;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.commands.command.PureTicketsCommand;
import broccolai.tickets.core.commands.command.TicketCommand;
import broccolai.tickets.core.commands.command.TicketsCommand;
import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.locale.Message;
import broccolai.tickets.core.service.MessageService;
import broccolai.tickets.core.ticket.TicketManager;
import broccolai.tickets.core.user.UserManager;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.extra.confirmation.CommandConfirmationManager;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

public abstract class TicketsCommandManager<C> {

    /**
     * Initialise ticket command manager
     *
     * @param pureTickets   PureTickets instance
     * @param config        Config instance
     * @param eventBus      Event bus
     * @param userManager   User manager
     * @param ticketManager Ticket manager
     * @param jdbi          Jdbi instance
     */
    public void initialise(
            final @NonNull PureTickets<C, ?, ?> pureTickets,
            final @NonNull Config config,
            final @NonNull TicketsEventBus eventBus,
            final @NonNull UserManager<C, ?, ?> userManager,
            final @NonNull TicketManager ticketManager,
            final @NonNull MessageService messageService,
            final @NonNull Jdbi jdbi
    ) {
        CommandManager<@NonNull OnlineSoul> cloudManager = this.getCommandManager();

        cloudManager.registerCommandPreProcessor(context -> {
            CommandContext<@NonNull OnlineSoul> commandContext = context.getCommandContext();
            commandContext.store("ticketManager", ticketManager);
            commandContext.store("userManager", userManager);
        });

        CommandConfirmationManager<@NonNull OnlineSoul> confirmationManager = new CommandConfirmationManager<>(
                30L,
                TimeUnit.SECONDS,
                context -> context.getCommandContext()
                        .getSender()
                        .sendMessage(Component.text("Confirmation required! run /puretickets confirm")),
                sender -> sender.sendMessage(Component.text("You don't have any pending commands"))
        );

        confirmationManager.registerConfirmationProcessor(cloudManager);

        new PureTicketsCommand(cloudManager, pureTickets, confirmationManager, jdbi);
        new TicketCommand(config, messageService, eventBus, ticketManager);
        new TicketsCommand(config, eventBus, userManager, ticketManager);

        //todo: register commands

        new MinecraftExceptionHandler<@NonNull OnlineSoul>()
                .withHandler(MinecraftExceptionHandler.ExceptionType.NO_PERMISSION, (soul, ex) -> {
                    return Message.EXCEPTION__NO_PERMISSION.use();
                })
                .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SENDER, (soul, ex) -> {
                    InvalidCommandSenderException icse = (InvalidCommandSenderException) ex;
                    String className = icse.getRequiredSender().getSimpleName();
                    return Message.EXCEPTION__INVALID_SENDER.use(Template.of("sender", className));
                })
                .withHandler(MinecraftExceptionHandler.ExceptionType.COMMAND_EXECUTION, (soul, ex) -> {
                    PureException exception = (PureException) ex.getCause();
                    return exception.getComponent();
                })
                .withArgumentParsingHandler()
                .withInvalidSyntaxHandler()
                .apply(cloudManager, ForwardingAudience.Single::audience);
    }

    protected abstract @NonNull CommandManager<@NonNull OnlineSoul> getCommandManager();

}
