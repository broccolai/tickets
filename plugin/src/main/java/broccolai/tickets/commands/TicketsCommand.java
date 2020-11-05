package broccolai.tickets.commands;

import broccolai.corn.core.Lists;
import broccolai.tickets.commands.arguments.TicketArgument;
import broccolai.tickets.configuration.Config;
import broccolai.tickets.exceptions.PureException;
import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.locale.MessageNames;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.storage.TimeAmount;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.ticket.TicketStats;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.user.PlayerSoul;
import broccolai.tickets.user.Soul;
import broccolai.tickets.user.UserManager;
import broccolai.tickets.utilities.Constants;
import broccolai.tickets.utilities.ReplacementUtilities;
import broccolai.tickets.utilities.UserUtilities;
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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class TicketsCommand extends BaseCommand {

    private final NotificationManager notificationManager;
    private final TicketManager ticketManager;

    /**
     * Create a new Tickets Command.
     *
     * @param manager             Command Manager
     * @param config              Config Instance
     * @param userManager         User Manager
     * @param notificationManager Notification Manager
     * @param ticketManager       Ticket Manager
     */
    public TicketsCommand(
            final @NonNull CommandManager manager, final @NonNull Config config, final @NonNull UserManager userManager,
            final @NonNull NotificationManager notificationManager, final @NonNull TicketManager ticketManager
    ) {
        this.notificationManager = notificationManager;
        this.ticketManager = ticketManager;

        final Command.Builder<Soul> builder = manager.commandBuilder("tickets", "tis");
        final CommandArgument<Soul, OfflinePlayer> targetArgument = manager.argumentBuilder(OfflinePlayer.class, "target")
//                .withSuggestionsProvider((context, input) -> userManager.getNames())
                .build();

        manager.command(builder.literal(
                config.getAliasShow().getFirst(),
                Description.of("Show a ticket"),
                config.getAliasShow().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".show")
                .argument(targetArgument.copy())
                .argument(TicketArgument.of(false, false))
                .handler(c -> processShow(c.getSender(), c.get("ticket"))));

        manager.command(builder.literal(
                config.getAliasPick().getFirst(),
                Description.of("Pick a ticket"),
                config.getAliasPick().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".pick")
                .argument(targetArgument.copy())
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN))
                .handler(this::processPick)
                .build());

        manager.command(builder.literal(
                config.getAliasAssign().getFirst(),
                Description.of("Assign a ticket"),
                config.getAliasAssign().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".assign")
                .argument(OfflinePlayerArgument.of("staff"))
                .argument(targetArgument.copy())
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN))
                .handler(this::processAssign)
                .build());

        manager.command(builder.literal(
                config.getAliasDone().getFirst(),
                Description.of("Done-mark a ticket"),
                config.getAliasDone().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".done")
                .argument(targetArgument.copy())
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED))
                .handler(this::processDone)
                .build());

        manager.command(builder.literal(
                config.getAliasYield().getFirst(),
                Description.of("Yield a ticket"),
                config.getAliasYield().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".yield")
                .argument(targetArgument.copy())
                .argument(TicketArgument.of(false, false, TicketStatus.PICKED))
                .handler(this::processYield)
                .build());

        manager.command(builder.literal(
                config.getAliasNote().getFirst(),
                Description.of("Add a note to a ticket"),
                config.getAliasNote().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".note")
                .argument(targetArgument.copy())
                .argument(TicketArgument.of(true, false))
                .argument(StringArgument.of("message"))
                .handler(this::processNote)
                .build());

        manager.command(builder.literal(
                config.getAliasReopen().getFirst(),
                Description.of("Reopen a ticket"),
                config.getAliasReopen().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".reopen")
                .argument(targetArgument.copy())
                .argument(TicketArgument.of(false, false, TicketStatus.CLOSED))
                .handler(this::processReopen)
                .build());

        manager.command(builder.literal(
                config.getAliasTeleport().getFirst(),
                Description.of("Teleport to a tickets creation location"),
                config.getAliasTeleport().getSecond()
        )
                .senderType(PlayerSoul.class)
                .permission(Constants.STAFF_PERMISSION + ".teleport")
                .argument(targetArgument.copy())
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED, TicketStatus.CLOSED))
                .handler(this::processTeleport)
                .build());

        manager.command(builder.literal(
                config.getAliasLog().getFirst(),
                Description.of("View a tickets log"),
                config.getAliasLog().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".log")
                .argument(targetArgument.copy())
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED, TicketStatus.CLOSED))
                .handler(c -> processLog(c.getSender(), c.get("ticket")))
                .build());

        manager.command(builder.literal(
                config.getAliasList().getFirst(),
                Description.of("List tickets"),
                config.getAliasList().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".list")
                .flag(manager.flagBuilder("status")
                        .withArgument(EnumArgument.of(TicketStatus.class, "status")))
                .flag(manager.flagBuilder("player")
                        .withArgument(OfflinePlayerArgument.of("player")))
                .flag(manager.flagBuilder("onlineOnly")
                        .withArgument(BooleanArgument.of("onlineOnly")))
                .handler(this::processList));

        manager.command(builder.literal(
                config.getAliasStatus().getFirst(),
                Description.of("View amount of tickets in"),
                config.getAliasStatus().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".status")
                .argument(targetArgument.copy())
                .handler(this::processStatus)
                .build());

        manager.command(builder.literal(
                config.getAliasHighscore().getFirst(),
                Description.of("View highscores of ticket completions"),
                config.getAliasHighscore().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".highscore")
                .argument(EnumArgument.optional(TimeAmount.class, "amount"))
                .handler(this::processHighscore)
                .build());
    }

    private void processPick(final @NonNull CommandContext<Soul> c) {
        Soul soul = c.getSender();
        Ticket ticket = c.get("ticket");

        try {
            ticket.pick(soul.getUniqueId());
            notificationManager.send(soul, ticket.getPlayerUUID(), MessageNames.PICK_TICKET, ticket);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processAssign(final @NonNull CommandContext<Soul> c) {
        Soul soul = c.getSender();
        Ticket ticket = c.get("ticket");
        OfflinePlayer staff = c.get("staff");

        try {
            ticket.pick(staff.getUniqueId());
            notificationManager.send(soul, staff.getUniqueId(), MessageNames.ASSIGN_TICKET, ticket);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processDone(final @NonNull CommandContext<Soul> c) {
        Soul soul = c.getSender();
        Ticket ticket = c.get("ticket");

        try {
            ticket.done(soul.getUniqueId());
            notificationManager.send(soul, ticket.getPlayerUUID(), MessageNames.DONE_TICKET, ticket);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processYield(final @NonNull CommandContext<Soul> c) {
        Soul soul = c.getSender();
        Ticket ticket = c.get("ticket");

        try {
            ticket.yield(soul.getUniqueId());
            notificationManager.send(soul, ticket.getPlayerUUID(), MessageNames.YIELD_TICKET, ticket);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processNote(final @NonNull CommandContext<Soul> c) {
        Soul soul = c.getSender();
        Ticket ticket = c.get("ticket");

        ticket.note(soul.getUniqueId(), c.get("message"));
        notificationManager.send(soul, ticket.getPlayerUUID(), MessageNames.NOTE_TICKET, ticket);
    }

    private void processReopen(final @NonNull CommandContext<Soul> c) {
        Soul soul = c.getSender();
        Ticket ticket = c.get("ticket");

        try {
            ticket.reopen(soul.getUniqueId());
            notificationManager.send(soul, ticket.getPlayerUUID(), MessageNames.REOPEN_TICKET, ticket);
        } catch (PureException e) {
            notificationManager.handleException(soul, e);
        }
    }

    private void processTeleport(final @NonNull CommandContext<Soul> c) {
        PlayerSoul soul = (PlayerSoul) c.getSender();
        Ticket ticket = c.get("ticket");

        notificationManager.send(soul, ticket.getPlayerUUID(), MessageNames.TELEPORT_TICKET, ticket);
        PaperLib.teleportAsync(soul.asPlayer(), ticket.getLocation());
    }

    private void processList(final @NonNull CommandContext<Soul> c) {
        final Soul soul = c.getSender();
        final OfflinePlayer player = c.flags().getValue("player", null);
        final Boolean onlineOnly = c.flags().getValue("onlineOnly", false);
        final TicketStatus[] statuses = statusesFromFlags(c.flags());

        if (player != null) {
            soul.message(Messages.TITLES__SPECIFIC_TICKETS, "player", player.getName());

            ticketManager.getTickets(player.getUniqueId(), statuses).forEach(ticket -> {
                String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
                soul.message(Messages.GENERAL__LIST_FORMAT, replacements);
            });
            return;
        }

        soul.message(Messages.TITLES__ALL_TICKETS);

        // todo: ugly
        Set<Map.Entry<UUID, List<Ticket>>> unsortedTickets = Lists
                .group(ticketManager.getTickets(statuses), Ticket::getPlayerUUID)
                .entrySet();
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

    private void processStatus(final @NonNull CommandContext<Soul> c) {
        Soul soul = c.getSender();
        OfflinePlayer target = c.getOrDefault("target", null);

        if (target != null) {
            soul.message(Messages.TITLES__SPECIFIC_TICKETS, "player", target.getName());
        } else {
            soul.message(Messages.TITLES__TICKET_STATUS);
        }

        TicketStats data;

        if (target != null) {
            data = ticketManager.getStats(target.getUniqueId());
        } else {
            data = ticketManager.getStats();
        }

        data.forEach((status, amount) -> {
            if (amount != 0) {
                soul.message(amount.toString() + " " + status.name().toLowerCase());
            }
        });
    }

    private void processHighscore(final @NonNull CommandContext<Soul> c) {
        Soul soul = c.getSender();
        TimeAmount amount = c.get("amount");

        Map<UUID, Integer> highscores = ticketManager.getHighscores(amount);
        soul.message(Messages.TITLES__HIGHSCORES);

        highscores.forEach((uuid, number) ->
                soul.message(Messages.GENERAL__HS_FORMAT, "target", UserUtilities.nameFromUUID(uuid), "amount", number.toString())
        );
    }

}
