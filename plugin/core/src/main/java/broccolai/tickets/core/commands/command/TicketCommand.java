package broccolai.tickets.core.commands.command;

import broccolai.tickets.core.commands.arguments.MessageArgument;
import broccolai.tickets.core.commands.arguments.TicketArgument;
import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.events.api.NotificationEvent;
import broccolai.tickets.core.events.api.TicketConstructionEvent;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.interactions.NotificationReason;
import broccolai.tickets.core.locale.Message;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.ticket.TicketManager;
import broccolai.tickets.core.ticket.TicketStatus;
import broccolai.tickets.core.user.PlayerSoul;
import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.UserManager;
import broccolai.tickets.core.utilities.Constants;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.Description;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public final class TicketCommand<C> extends BaseCommand<C> {

    private final TicketsEventBus eventManager;
    private final TicketManager ticketManager;

    /**
     * Create a new Ticket Command
     *
     * @param manager       Command Manager
     * @param config        Config instance
     * @param eventManager  Event Manager
     * @param userManager   User Manager
     * @param ticketManager Ticket Manager
     */
    public TicketCommand(
            final @NonNull CommandManager<Soul<C>> manager,
            final @NonNull Config config,
            final @NonNull TicketsEventBus eventManager,
            final @NonNull UserManager<C, ?, ?> userManager,
            final @NonNull TicketManager ticketManager
    ) {
        super(userManager);
        this.eventManager = eventManager;
        this.ticketManager = ticketManager;

        final Command.Builder<Soul<C>> builder = manager.commandBuilder("ticket", "ti")
                .senderType(userManager.getPlayerSoulClass());

        manager.command(builder.literal(
                config.getAliasCreate().getFirst(),
                Description.of("Create a ticket"),
                config.getAliasCreate().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".create")
                .argument(MessageArgument.of("message"))
                .handler(this::processCreate));

        manager.command(builder.literal(
                config.getAliasUpdate().getFirst(),
                Description.of("Update a ticket"),
                config.getAliasUpdate().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".update")
                .argument(TicketArgument.of(true, true, TicketStatus.OPEN, TicketStatus.PICKED))
                .argument(MessageArgument.of("message"))
                .handler(this::processUpdate));

        manager.command(builder.literal(
                config.getAliasClose().getFirst(),
                Description.of("Close a ticket"),
                config.getAliasClose().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".close")
                .argument(TicketArgument.of(false, true, TicketStatus.OPEN, TicketStatus.PICKED))
                .handler(this::processClose));

        manager.command(builder.literal(
                config.getAliasList().getFirst(),
                Description.of("List tickets"),
                config.getAliasList().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".list")
                .flag(manager.flagBuilder("status")
                        .withArgument(EnumArgument.of(TicketStatus.class, "status")))
                .handler(this::processList));

        manager.command(builder.literal(
                config.getAliasShow().getFirst(),
                Description.of("Show a ticket"),
                config.getAliasShow().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".show")
                .argument(TicketArgument.of(false, true))
                .handler(c -> processShow(c.getSender(), c.get("ticket"))));

        manager.command(builder.literal(
                config.getAliasLog().getFirst(),
                Description.of("View a tickets log"),
                config.getAliasLog().getSecond()
        )
                .permission(Constants.USER_PERMISSION + ".log")
                .argument(TicketArgument.of(false, true))
                .handler(c -> processLog(c.getSender(), c.get("ticket"))));
    }

    private void processCreate(final @NonNull CommandContext<Soul<C>> c) {
        PlayerSoul<C, ?> soul = (PlayerSoul<C, ?>) c.getSender();
        TicketConstructionEvent constructionEvent = new TicketConstructionEvent(soul, c.get("message"));
        this.eventManager.post(constructionEvent);

        constructionEvent.getException()
                .map(PureException::getComponent)
                .ifPresent(soul::sendActionBar);
    }

    private void processUpdate(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        try {
            ticket.update(c.get("message"));
            this.eventManager.post(new NotificationEvent(NotificationReason.UPDATE_TICKET, soul, null, ticket));
        } catch (PureException e) {
            soul.sendMessage(e.getComponent());
        }
    }

    private void processClose(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        try {
            ticket.close(soul.getUniqueId());
            this.eventManager.post(new NotificationEvent(NotificationReason.CLOSE_TICKET, soul, null, ticket));
        } catch (PureException e) {
            soul.sendMessage(e.getComponent());
        }
    }

    private void processList(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        TicketStatus status = c.flags().getValue("status", null);
        List<Ticket> tickets = ticketManager.getTickets(
                soul.getUniqueId(),
                status != null ? status : TicketStatus.OPEN,
                TicketStatus.PICKED
        );

        TextComponent.Builder builder = Component.text()
                .append(Message.TITLE__YOUR_TICKETS.use());

        tickets.forEach(ticket -> {
            Component list = Message.FORMAT__LIST.use(ticket.templates());
            builder.append(Component.newline(), list);
        });

        soul.sendMessage(builder);
    }

}
