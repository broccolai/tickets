package broccolai.tickets.core.commands;

import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.commands.command.PureTicketsCommand;
import broccolai.tickets.core.commands.command.TicketCommand;
import broccolai.tickets.core.commands.command.TicketsCommand;
import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.ticket.TicketManager;
import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.UserManager;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

public abstract class TicketsCommandManager<C> {

    /**
     * Initialise ticket command manager
     *
     * @param pureTickets   PureTickets instance
     * @param config        Config instance
     * @param eventBus      Event bus
     * @param userManager   User manager
     * @param ticketManager Ticket manager
     */
    public void initialise(
            final @NonNull PureTickets<C, ?, ?> pureTickets,
            final @NonNull Config config,
            final @NonNull TicketsEventBus eventBus,
            final @NonNull UserManager<C, ?, ?> userManager,
            final @NonNull TicketManager ticketManager
    ) {
        CommandManager<Soul<C>> cloudManager = this.getCommandManager(userManager::fromSender, Soul::asSender);

        cloudManager.registerCommandPreProcessor(context -> {
            CommandContext<Soul<C>> commandContext = context.getCommandContext();
            commandContext.store("ticketManager", ticketManager);
            commandContext.store("userManager", userManager);
        });

        new PureTicketsCommand<>(cloudManager, pureTickets);
        new TicketCommand<>(cloudManager, config, eventBus, userManager, ticketManager);
        new TicketsCommand<>(cloudManager, config, eventBus, userManager, ticketManager);
    }

    protected abstract @NonNull CommandManager<Soul<C>> getCommandManager(
            @NonNull Function<C, Soul<C>> commandSenderMapper,
            @NonNull Function<Soul<C>, C> backwardsCommandSenderMapper
    );

}
