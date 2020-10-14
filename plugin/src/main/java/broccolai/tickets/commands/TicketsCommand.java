package broccolai.tickets.commands;

import broccolai.corn.core.Lists;
import broccolai.tickets.commands.arguments.TicketArgument;
import broccolai.tickets.configuration.Config;
import broccolai.tickets.exceptions.PureException;
import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.locale.MessageNames;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.storage.TimeAmount;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.user.PlayerSoul;
import broccolai.tickets.user.Soul;
import broccolai.tickets.user.UserManager;
import broccolai.tickets.utilities.Constants;
import broccolai.tickets.utilities.generic.ReplacementUtilities;
import broccolai.tickets.utilities.generic.UserUtilities;
import cloud.commandframework.Command;
import cloud.commandframework.Description;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.parsers.OfflinePlayerArgument;
import cloud.commandframework.context.CommandContext;
import com.google.common.collect.ImmutableMap;
import io.papermc.lib.PaperLib;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Command used for staff to interact with other players tickets.
 */
public final class TicketsCommand extends BaseCommand {
    @NotNull
    private final TicketManager ticketManager;
    @NotNull
    private final NotificationManager notificationManager;

    /**
     * Create a new Tickets Command.
     *
     * @param manager             Command Manager
     * @param config              Config Instance
     * @param userManager         User Manager
     * @param ticketManager       Ticket Manager
     * @param notificationManager Notification Manager
     */
    public TicketsCommand(@NotNull final CommandManager manager, @NotNull final Config config, @NotNull final UserManager userManager,
                          @NotNull final TicketManager ticketManager, @NotNull final NotificationManager notificationManager) {
        this.ticketManager = ticketManager;
        this.notificationManager = notificationManager;

        final Command.Builder<Soul> builder = manager.commandBuilder("tickets", "tis");
        final CommandArgument<Soul, OfflinePlayer> targetArgument = manager.argumentBuilder(OfflinePlayer.class, "target")
            .withSuggestionsProvider((context, input) -> userManager.getNames())
            .build();

        manager.command(builder.literal(config.ALIAS__SHOW.getFirst(), Description.of("Show a ticket"), config.ALIAS__SHOW.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".show")
            .argument(targetArgument.copy())
            .argument(TicketArgument.of(false, false))
            .handler(c -> processShow(c.getSender(), c.get("ticket"))));

        manager.command(builder.literal(config.ALIAS__PICK.getFirst(), Description.of("Pick a ticket"), config.ALIAS__PICK.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".pick")
            .argument(targetArgument.copy())
            .argument(TicketArgument.of(false, false, TicketStatus.OPEN))
            .handler(this::processPick)
            .build());

        manager.command(builder.literal(config.ALIAS__ASSIGN.getFirst(), Description.of("Assign a ticket"), config.ALIAS__ASSIGN.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".assign")
            .argument(OfflinePlayerArgument.of("staff"))
            .argument(targetArgument.copy())
            .argument(TicketArgument.of(false, false, TicketStatus.OPEN))
            .handler(this::processAssign)
            .build());

        manager.command(builder.literal(config.ALIAS__DONE.getFirst(), Description.of("Done-mark a ticket"), config.ALIAS__DONE.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".done")
            .argument(targetArgument.copy())
            .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED))
            .handler(this::processDone)
            .build());

        manager.command(builder.literal(config.ALIAS__YIELD.getFirst(), Description.of("Yield a ticket"), config.ALIAS__YIELD.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".yield")
            .argument(targetArgument.copy())
            .argument(TicketArgument.of(false, false, TicketStatus.PICKED))
            .handler(this::processYield)
            .build());

        manager.command(builder.literal(config.ALIAS__NOTE.getFirst(), Description.of("Add a note to a ticket"), config.ALIAS__NOTE.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".note")
            .argument(targetArgument.copy())
            .argument(TicketArgument.of(true, false))
            .argument(StringArgument.of("message"))
            .handler(this::processNote)
            .build());

        manager.command(builder.literal(config.ALIAS__REOPEN.getFirst(), Description.of("Reopen a ticket"), config.ALIAS__REOPEN.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".reopen")
            .argument(targetArgument.copy())
            .argument(TicketArgument.of(false, false, TicketStatus.CLOSED))
            .handler(this::processReopen)
            .build());

        manager.command(builder.literal(config.ALIAS__TELEPORT.getFirst(), Description.of("Teleport to a tickets creation location"), config.ALIAS__TELEPORT.getSecond())
            .senderType(PlayerSoul.class)
            .permission(Constants.STAFF_PERMISSION + ".teleport")
            .argument(targetArgument.copy())
            .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED, TicketStatus.CLOSED))
            .handler(this::processTeleport)
            .build());

        manager.command(builder.literal(config.ALIAS__LOG.getFirst(), Description.of("View a tickets log"), config.ALIAS__LOG.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".log")
            .argument(targetArgument.copy())
            .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED, TicketStatus.CLOSED))
            .handler(c -> processLog(c.getSender(), c.get("ticket")))
            .build());

        manager.command(builder.literal(config.ALIAS__LIST.getFirst(), Description.of("List tickets"), config.ALIAS__LIST.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".list")
            .flag(manager.flagBuilder("status")
                .withArgument(EnumArgument.of(TicketStatus.class, "status")))
            .flag(manager.flagBuilder("player")
                .withArgument(OfflinePlayerArgument.of("player")))
            .flag(manager.flagBuilder("onlineOnly")
                .withArgument(BooleanArgument.of("onlineOnly")))
            .handler(this::processList));

        manager.command(builder.literal(config.ALIAS__STATUS.getFirst(), Description.of("View amount of tickets in"), config.ALIAS__STATUS.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".status")
            .argument(targetArgument.copy())
            .handler(this::processStatus)
            .build());

        manager.command(builder.literal(config.ALIAS__HIGHSCORE.getFirst(), Description.of("View highscores of ticket completions"), config.ALIAS__HIGHSCORE.getSecond())
            .permission(Constants.STAFF_PERMISSION + ".highscore")
            .argument(EnumArgument.optional(TimeAmount.class, "amount"))
            .handler(this::processHighscore)
            .build());
    }

    private void processPick(@NotNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();
        Ticket modified = ticketManager.pick(soul.getUniqueId(), c.get("ticket"));

        notificationManager.send(soul, modified.getPlayerUUID(), MessageNames.PICK_TICKET, modified);
    }

    private void processAssign(@NotNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();
        OfflinePlayer staff = c.get("staff");

        try {
            Ticket modified = ticketManager.pick(staff.getUniqueId(), c.get("ticket"));

            notificationManager.send(soul, staff.getUniqueId(), MessageNames.ASSIGN_TICKET, modified);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processDone(@NotNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();

        try {
            Ticket modified = ticketManager.done(soul.getUniqueId(), c.get("ticket"));

            notificationManager.send(soul, modified.getPlayerUUID(), MessageNames.DONE_TICKET, modified);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processYield(@NotNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();

        try {
            Ticket modified = ticketManager.yield(soul.getUniqueId(), c.get("ticket"));

            notificationManager.send(soul, modified.getPlayerUUID(), MessageNames.YIELD_TICKET, modified);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processNote(@NotNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();
        Ticket modified = ticketManager.note(soul.getUniqueId(), c.get("ticket"), c.get("message"));

        notificationManager.send(soul, modified.getPlayerUUID(), MessageNames.NOTE_TICKET, modified);
    }

    private void processReopen(@NotNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();

        try {
            Ticket modified = ticketManager.reopen(soul.getUniqueId(), c.get("ticket"));

            notificationManager.send(soul, modified.getPlayerUUID(), MessageNames.REOPEN_TICKET, modified);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processTeleport(@NotNull final CommandContext<Soul> c) {
        PlayerSoul soul = (PlayerSoul) c.getSender();
        Ticket ticket = c.get("ticket");

        notificationManager.send(soul, ticket.getPlayerUUID(), MessageNames.TELEPORT_TICKET, ticket);
        PaperLib.teleportAsync(soul.asPlayer(), ticket.getLocation());
    }

    private void processList(@NotNull final CommandContext<Soul> c) {
        final Soul soul = c.getSender();
        final OfflinePlayer player = c.flags().getValue("player", null);
        final TicketStatus status = c.flags().getValue("status", null);
        final Boolean onlineOnly = c.flags().getValue("onlineOnly", false);

        if (player != null) {
            soul.message(Messages.TITLES__SPECIFIC_TICKETS, "player", player.getName());

            TicketSQL.selectAll(player.getUniqueId(), status).forEach((ticket -> {
                String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
                soul.message(Messages.GENERAL__LIST_FORMAT, replacements);
            }));
            return;
        }

        soul.message(Messages.TITLES__ALL_TICKETS);

        // todo: ugly
        Set<Map.Entry<UUID, List<Ticket>>> unsortedTickets = Lists.group(TicketSQL.selectAll(status), Ticket::getPlayerUUID).entrySet();
        List<Map.Entry<UUID, List<Ticket>>> sortedTickets = new ArrayList<>(unsortedTickets);
        sortedTickets.sort((t1, t2) -> {
            Integer boxed = t1.getValue().get(0).getId();
            return boxed.compareTo(t2.getValue().get(0).getId());
        });

        ImmutableMap.copyOf(sortedTickets).forEach((uuid, tickets) -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            //noinspection ConstantConditions
            if (onlineOnly && !offlinePlayer.isOnline()) {
                return;
            }

            soul.message(Messages.GENERAL__LIST_HEADER_FORMAT, "name", UserUtilities.nameFromUUID(uuid));

            tickets.forEach(ticket -> {
                String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
                soul.message(Messages.GENERAL__LIST_FORMAT, replacements);
            });
        });
    }

    private void processStatus(@NotNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();
        OfflinePlayer target = c.getOrDefault("target", null);

        if (target != null) {
            soul.message(Messages.TITLES__SPECIFIC_TICKETS, "player", target.getName());
        } else {
            soul.message(Messages.TITLES__TICKET_STATUS);
        }

        EnumMap<TicketStatus, Integer> data = TicketSQL.selectTicketStats(target != null ? target.getUniqueId() : null);

        data.forEach((status, amount) -> {
            if (amount != 0) {
                soul.message(amount.toString() + " " + status.name().toLowerCase());
            }
        });
    }

    private void processHighscore(@NotNull final CommandContext<Soul> c) {
        Soul soul = c.getSender();
        TimeAmount amount = c.get("amount");

        Map<UUID, Integer> highscores = TicketSQL.highscores(amount);
        soul.message(Messages.TITLES__HIGHSCORES);

        highscores.forEach((uuid, number) ->
            soul.message(Messages.GENERAL__HS_FORMAT, "target", UserUtilities.nameFromUUID(uuid), "amount", number.toString())
        );
    }
}
