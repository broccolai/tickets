package broccolai.tickets.core.commands.command;

import broccolai.tickets.core.commands.arguments.MessageArgument;
import broccolai.tickets.core.commands.arguments.TicketArgument;
import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.events.api.NotificationEvent;
import broccolai.tickets.core.events.api.TicketConstructionEvent;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.interactions.NotificationReason;
import broccolai.tickets.core.model.user.PlayerUserAudience;
import broccolai.tickets.core.model.user.UserAudience;
import broccolai.tickets.core.service.MessageService;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.ticket.TicketManager;
import broccolai.tickets.core.ticket.TicketStatus;
import broccolai.tickets.core.user.UserManager;
import broccolai.tickets.core.utilities.Constants;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public final class TicketCommand<C> extends CommonCommands {

    private final Config config;
    private final MessageService messageService;
    private final TicketsEventBus eventManager;
    private final UserManager<C, ?, ?> userManager;
    private final TicketManager ticketManager;

    /**
     * Create a new Ticket Command
     *
     * @param config        Config instance
     * @param eventManager  Event Manager
     * @param userManager   User Manager
     * @param ticketManager Ticket Manager
     */
    public TicketCommand(
            final @NonNull Config config,
            final @NonNull MessageService messageService,
            final @NonNull TicketsEventBus eventManager,
            final @NonNull UserManager<C, ?, ?> userManager,
            final @NonNull TicketManager ticketManager
    ) {
        this.config = config;
        this.messageService = messageService;
        this.eventManager = eventManager;
        this.userManager = userManager;
        this.ticketManager = ticketManager;
    }

    @Override
    public void register(
            @NonNull final CommandManager<@NonNull UserAudience> manager
    ) {
        final Command.Builder<UserAudience> builder = manager.commandBuilder("ticket", "ti")
                .senderType(PlayerUserAudience.class);

        manager.command(builder.literal(
                config.getAliasCreate().getFirst(),
                ArgumentDescription.of("Create a ticket"),
                config.getAliasCreate().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".create")
                .argument(MessageArgument.of("message"))
                .handler(this::processCreate));

        manager.command(builder.literal(
                config.getAliasUpdate().getFirst(),
                ArgumentDescription.of("Update a ticket"),
                config.getAliasUpdate().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".update")
                .argument(TicketArgument.of(true, true, TicketStatus.OPEN, TicketStatus.PICKED))
                .argument(MessageArgument.of("message"))
                .handler(this::processUpdate));

        manager.command(builder.literal(
                config.getAliasClose().getFirst(),
                ArgumentDescription.of("Close a ticket"),
                config.getAliasClose().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".close")
                .argument(TicketArgument.of(false, true, TicketStatus.OPEN, TicketStatus.PICKED))
                .handler(this::processClose));

        manager.command(builder.literal(
                config.getAliasList().getFirst(),
                ArgumentDescription.of("List tickets"),
                config.getAliasList().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".list")
                .flag(manager.flagBuilder("status")
                        .withArgument(EnumArgument.of(TicketStatus.class, "status")))
                .handler(this::processList));

        manager.command(builder.literal(
                config.getAliasShow().getFirst(),
                ArgumentDescription.of("Show a ticket"),
                config.getAliasShow().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".show")
                .argument(TicketArgument.of(false, true))
                .handler(c -> processShow(c.getSender(), c.get("ticket"))));

        manager.command(builder.literal(
                config.getAliasLog().getFirst(),
                ArgumentDescription.of("View a tickets log"),
                config.getAliasLog().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".log")
                .argument(TicketArgument.of(false, true))
                .handler(c -> processLog(c.getSender(), c.get("ticket"))));
    }

    private void processCreate(final @NonNull CommandContext<UserAudience> c) {
        PlayerUserAudience playerUser = (PlayerUserAudience) c.getSender();
        TicketConstructionEvent constructionEvent = new TicketConstructionEvent(playerUser, c.get("message"));
        this.eventManager.post(constructionEvent);

        constructionEvent.getException()
                .map(PureException::getComponent)
                .ifPresent(soul::sendMessage);
    }

    private void processUpdate(final @NonNull CommandContext<UserAudience> c) {
        UserAudience soul = c.getSender();
        Ticket ticket = c.get("ticket");
        broccolai.tickets.core.message.Message message = c.get("message");

        ticket.update(message);
        this.eventManager.post(new NotificationEvent(NotificationReason.UPDATE_TICKET, soul, null, ticket));
        ticketManager.updateTicket(ticket);
        ticketManager.insertMessage(ticket.getId(), message);
    }

    private void processClose(final @NonNull CommandContext<UserAudience> c) {
        UserAudience soul = c.getSender();
        Ticket ticket = c.get("ticket");

        ticketManager.insertMessage(ticket.getId(), ticket.close(soul.uuid()));
        ticketManager.updateTicket(ticket);
        this.eventManager.post(new NotificationEvent(NotificationReason.CLOSE_TICKET, soul, null, ticket));
    }

    private void processList(final @NonNull CommandContext<UserAudience> c) {
        UserAudience soul = c.getSender();
        TicketStatus status = c.flags().getValue("status", null);
        List<Ticket> tickets = this.ticketManager.getTickets(
                soul.uuid(),
                status != null ? status : TicketStatus.OPEN,
                TicketStatus.PICKED
        );

        Component component = this.messageService.commandsTicketList(tickets);
        soul.sendMessage(component);
    }

}
