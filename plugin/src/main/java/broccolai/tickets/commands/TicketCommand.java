package broccolai.tickets.commands;

import broccolai.tickets.commands.arguments.MessageArgument;
import broccolai.tickets.commands.arguments.TicketArgument;
import broccolai.tickets.configuration.Config;
import broccolai.tickets.events.TicketConstructionEvent;
import broccolai.tickets.exceptions.PureException;
import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.locale.MessageNames;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.user.PlayerSoul;
import broccolai.tickets.user.Soul;
import broccolai.tickets.utilities.Constants;
import broccolai.tickets.utilities.generic.ReplacementUtilities;
import cloud.commandframework.Command;
import cloud.commandframework.Description;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import java.util.List;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public final class TicketCommand extends BaseCommand {
    @NotNull
    private final PluginManager pluginManager;
    @NotNull
    private final TicketManager ticketManager;
    @NotNull
    private final NotificationManager notificationManager;

    /**
     * Create a new Ticket Command.
     *
     * @param pluginManager       Plugin Manager
     * @param config              Config instance
     * @param manager             Command Manager
     * @param ticketManager       Ticket Manager
     * @param notificationManager Notification Manager
     */
    public TicketCommand(@NotNull final PluginManager pluginManager, @NotNull final Config config, @NotNull final CommandManager manager,
                         @NotNull final TicketManager ticketManager, @NotNull final NotificationManager notificationManager) {
        this.pluginManager = pluginManager;
        this.ticketManager = ticketManager;
        this.notificationManager = notificationManager;

        final Command.Builder<Soul> builder = manager.commandBuilder("ticket", "ti")
            .senderType(PlayerSoul.class);

        manager.command(builder.literal(config.ALIAS__CREATE.getFirst(), Description.of("Create a ticket"), config.ALIAS__CREATE.getSecond())
            .permission(Constants.USER_PERMISSION + ".create")
            .argument(new MessageArgument<>(true, "message"))
            .handler(this::processCreate));

        manager.command(builder.literal(config.ALIAS__UPDATE.getFirst(), Description.of("Update a ticket"), config.ALIAS__UPDATE.getSecond())
            .permission(Constants.USER_PERMISSION + ".update")
            .argument(TicketArgument.of(true, true, TicketStatus.OPEN, TicketStatus.PICKED))
            .argument(new MessageArgument<>(true, "message"))
            .handler(this::processUpdate));

        manager.command(builder.literal(config.ALIAS__CLOSE.getFirst(), Description.of("Close a ticket"), config.ALIAS__CLOSE.getSecond())
            .permission(Constants.USER_PERMISSION + ".close")
            .argument(TicketArgument.of(false, true, TicketStatus.OPEN, TicketStatus.PICKED))
            .handler(this::processClose));

        manager.command(builder.literal(config.ALIAS__LIST.getFirst(), Description.of("List tickets"), config.ALIAS__LIST.getSecond())
            .permission(Constants.USER_PERMISSION + ".list")
            .flag(manager.flagBuilder("status")
                .withArgument(EnumArgument.of(TicketStatus.class, "status")))
            .handler(this::processList));

        manager.command(builder.literal(config.ALIAS__SHOW.getFirst(), Description.of("Show a ticket"), config.ALIAS__SHOW.getSecond())
            .permission(Constants.USER_PERMISSION + ".show")
            .argument(TicketArgument.of(false, true))
            .handler(c -> processShow(c.getSender(), c.get("ticket"))));

        manager.command(builder.literal(config.ALIAS__LOG.getFirst(), Description.of("View a tickets log"), config.ALIAS__LOG.getSecond())
            .permission(Constants.USER_PERMISSION + ".log")
            .argument(TicketArgument.of(false, true))
            .handler(c -> processLog(c.getSender(), c.get("ticket"))));
    }

    private void processCreate(@NotNull final CommandContext<Soul> c) {
        PlayerSoul soul = (PlayerSoul) c.getSender();
        TicketConstructionEvent constructionEvent = new TicketConstructionEvent(soul, c.get("message"));
        pluginManager.callEvent(constructionEvent);

        if (constructionEvent.hasException()) {
            notificationManager.handleException(soul, constructionEvent.getException());
        }
    }

    private void processUpdate(@NotNull final CommandContext<Soul> c) {
        try {
            Ticket edited = ticketManager.update(c.get("ticket"), c.get("message"));
            notificationManager.send(c.getSender(), null, MessageNames.UPDATE_TICKET, edited);
        } catch (PureException e) {
            notificationManager.handleException(c.getSender(), e);
        }
    }

    private void processClose(@NotNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();

        try {
            Ticket edited = ticketManager.close(soul.getUniqueId(), c.get("ticket"));
            notificationManager.send(soul, null, MessageNames.CLOSE_TICKET, edited);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processList(@NotNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();
        List<Ticket> tickets = TicketSQL.selectAll(soul.getUniqueId(), c.flags().getValue("status", null));

        soul.message(Messages.TITLES__YOUR_TICKETS);

        tickets.forEach(ticket -> {
            String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
            soul.message(Messages.GENERAL__LIST_FORMAT, replacements);
        });
    }
}
