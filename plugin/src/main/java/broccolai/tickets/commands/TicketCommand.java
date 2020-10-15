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
import broccolai.tickets.utilities.ReplacementUtilities;
import cloud.commandframework.Command;
import cloud.commandframework.Description;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import org.bukkit.plugin.PluginManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public final class TicketCommand extends BaseCommand {

    @NonNull
    private final PluginManager pluginManager;
    @NonNull
    private final TicketManager ticketManager;
    @NonNull
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
    public TicketCommand(
            @NonNull final PluginManager pluginManager, @NonNull final Config config, @NonNull final CommandManager manager,
            @NonNull final TicketManager ticketManager, @NonNull final NotificationManager notificationManager
    ) {
        this.pluginManager = pluginManager;
        this.ticketManager = ticketManager;
        this.notificationManager = notificationManager;

        final Command.Builder<Soul> builder = manager.commandBuilder("ticket", "ti")
                .senderType(PlayerSoul.class);

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

    private void processCreate(@NonNull final CommandContext<Soul> c) {
        PlayerSoul soul = (PlayerSoul) c.getSender();
        TicketConstructionEvent constructionEvent = new TicketConstructionEvent(soul, c.get("message"));
        pluginManager.callEvent(constructionEvent);

        if (constructionEvent.hasException()) {
            notificationManager.handleException(soul, constructionEvent.getException());
        }
    }

    private void processUpdate(@NonNull final CommandContext<Soul> c) {
        try {
            Ticket edited = ticketManager.update(c.get("ticket"), c.get("message"));
            notificationManager.send(c.getSender(), null, MessageNames.UPDATE_TICKET, edited);
        } catch (PureException e) {
            notificationManager.handleException(c.getSender(), e);
        }
    }

    private void processClose(@NonNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();

        try {
            Ticket edited = ticketManager.close(soul.getUniqueId(), c.get("ticket"));
            notificationManager.send(soul, null, MessageNames.CLOSE_TICKET, edited);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processList(@NonNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();
        List<Ticket> tickets = TicketSQL.selectAll(soul.getUniqueId(), c.flags().getValue("status", null));

        soul.message(Messages.TITLES__YOUR_TICKETS);

        tickets.forEach(ticket -> {
            String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
            soul.message(Messages.GENERAL__LIST_FORMAT, replacements);
        });
    }

}
