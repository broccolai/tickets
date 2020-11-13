package broccolai.tickets.core.commands.command;

import broccolai.corn.core.Lists;
import broccolai.tickets.core.commands.arguments.TargetArgument;
import broccolai.tickets.core.commands.arguments.TicketArgument;
import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.events.api.NotificationEvent;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.interactions.NotificationReason;
import broccolai.tickets.core.locale.Messages;
import broccolai.tickets.core.storage.TimeAmount;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.ticket.TicketManager;
import broccolai.tickets.core.ticket.TicketStats;
import broccolai.tickets.core.ticket.TicketStatus;
import broccolai.tickets.core.user.PlayerSoul;
import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.UserManager;
import broccolai.tickets.core.utilities.Constants;
import broccolai.tickets.core.utilities.ReplacementUtilities;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.Description;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class TicketsCommand<C> extends BaseCommand<C> {

    private final TicketsEventBus eventBus;
    private final TicketManager ticketManager;

    /**
     * Create a new Tickets Command.
     *
     * @param manager       Command Manager
     * @param config        Config Instance
     * @param eventBus      Event bus
     * @param userManager   User Manager
     * @param ticketManager Ticket Manager
     */
    public TicketsCommand(
            final @NonNull CommandManager<Soul<C>> manager,
            final @NonNull Config config,
            final @NonNull TicketsEventBus eventBus,
            final @NonNull UserManager<C, ?, ?> userManager,
            final @NonNull TicketManager ticketManager
    ) {
        super(userManager);
        this.eventBus = eventBus;
        this.ticketManager = ticketManager;

        final Command.Builder<Soul<C>> builder = manager.commandBuilder("tickets", "tis");

        manager.command(builder.literal(
                config.getAliasShow().getFirst(),
                Description.of("Show a ticket"),
                config.getAliasShow().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".show")
                .argument(TargetArgument.of("target"))
                .argument(TicketArgument.of(false, false))
                .handler(c -> processShow(c.getSender(), c.get("ticket"))));

        manager.command(builder.literal(
                config.getAliasPick().getFirst(),
                Description.of("Pick a ticket"),
                config.getAliasPick().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".pick")
                .argument(TargetArgument.of("target"))
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN))
                .handler(this::processPick)
                .build());

        manager.command(builder.literal(
                config.getAliasAssign().getFirst(),
                Description.of("Assign a ticket"),
                config.getAliasAssign().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".assign")
                .argument(TargetArgument.of("staff"))
                .argument(TargetArgument.of("target"))
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN))
                .handler(this::processAssign)
                .build());

        manager.command(builder.literal(
                config.getAliasDone().getFirst(),
                Description.of("Done-mark a ticket"),
                config.getAliasDone().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".done")
                .argument(TargetArgument.of("target"))
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED))
                .handler(this::processDone)
                .build());

        manager.command(builder.literal(
                config.getAliasYield().getFirst(),
                Description.of("Yield a ticket"),
                config.getAliasYield().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".yield")
                .argument(TargetArgument.of("target"))
                .argument(TicketArgument.of(false, false, TicketStatus.PICKED))
                .handler(this::processYield)
                .build());

        manager.command(builder.literal(
                config.getAliasNote().getFirst(),
                Description.of("Add a note to a ticket"),
                config.getAliasNote().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".note")
                .argument(TargetArgument.of("target"))
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
                .argument(TargetArgument.of("target"))
                .argument(TicketArgument.of(false, false, TicketStatus.CLOSED))
                .handler(this::processReopen)
                .build());

        manager.command(builder.literal(
                config.getAliasTeleport().getFirst(),
                Description.of("Teleport to a tickets creation location"),
                config.getAliasTeleport().getSecond()
        )
                .senderType(userManager.getPlayerSoulClass())
                .permission(Constants.STAFF_PERMISSION + ".teleport")
                .argument(TargetArgument.of("target"))
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED, TicketStatus.CLOSED))
                .handler(this::processTeleport)
                .build());

        manager.command(builder.literal(
                config.getAliasLog().getFirst(),
                Description.of("View a tickets log"),
                config.getAliasLog().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".log")
                .argument(TargetArgument.of("target"))
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
                        .withArgument(TargetArgument.of("player")))
                .flag(manager.flagBuilder("onlineOnly")
                        .withArgument(BooleanArgument.of("onlineOnly")))
                .handler(this::processList));

        manager.command(builder.literal(
                config.getAliasStatus().getFirst(),
                Description.of("View amount of tickets in"),
                config.getAliasStatus().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".status")
                .argument(TargetArgument.of("target"))
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

    private void processPick(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        try {
            ticket.pick(soul.getUniqueId());
            this.eventBus.post(new NotificationEvent(NotificationReason.PICK_TICKET, soul, ticket.getPlayerUUID(), ticket));
        } catch (PureException e) {
            soul.handleException(e);
        }
    }

    private void processAssign(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");
        UUID staff = c.get("staff");

        try {
            ticket.pick(staff);
            this.eventBus.post(new NotificationEvent(NotificationReason.ASSIGN_TICKET, soul, staff, ticket));
        } catch (PureException e) {
            soul.handleException(e);
        }
    }

    private void processDone(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        try {
            ticket.done(soul.getUniqueId());
            this.eventBus.post(new NotificationEvent(NotificationReason.DONE_TICKET, soul, ticket.getPlayerUUID(), ticket));
        } catch (PureException e) {
            soul.handleException(e);
        }
    }

    private void processYield(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        try {
            ticket.yield(soul.getUniqueId());
            this.eventBus.post(new NotificationEvent(NotificationReason.YIELD_TICKET, soul, ticket.getPlayerUUID(), ticket));
        } catch (PureException e) {
            soul.handleException(e);
        }
    }

    private void processNote(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        ticket.note(soul.getUniqueId(), c.get("message"));
        this.eventBus.post(new NotificationEvent(NotificationReason.NOTE_TICKET, soul, ticket.getPlayerUUID(), ticket));
    }

    private void processReopen(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        try {
            ticket.reopen(soul.getUniqueId());
            this.eventBus.post(new NotificationEvent(NotificationReason.REOPEN_TICKET, soul, ticket.getPlayerUUID(), ticket));
        } catch (PureException e) {
            soul.handleException(e);
        }
    }

    private void processTeleport(final @NonNull CommandContext<Soul<C>> c) {
        PlayerSoul<C, ?> soul = (PlayerSoul<C, ?>) c.getSender();
        Ticket ticket = c.get("ticket");

        this.eventBus.post(new NotificationEvent(NotificationReason.TELEPORT_TICKET, soul, ticket.getPlayerUUID(), ticket));
        soul.teleport(ticket.getLocation());
    }

    private void processList(final @NonNull CommandContext<Soul<C>> c) {
        final Soul<C> soul = c.getSender();
        final UUID player = c.flags().getValue("player", null);
        final Boolean onlineOnly = c.flags().getValue("onlineOnly", false);
        final TicketStatus[] statuses = statusesFromFlags(c.flags());

        if (player != null) {
            soul.message(Messages.TITLES__SPECIFIC_TICKETS, "player", this.userManager.getName(player));

            this.ticketManager.getTickets(player, statuses).forEach(ticket -> {
                String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
                soul.message(Messages.GENERAL__LIST_FORMAT, replacements);
            });
            return;
        }

        soul.message(Messages.TITLES__ALL_TICKETS);

        // todo: ugly
        Set<Map.Entry<UUID, List<Ticket>>> unsortedTickets = Lists
                .group(this.ticketManager.getTickets(statuses), Ticket::getPlayerUUID)
                .entrySet();
        List<Map.Entry<UUID, List<Ticket>>> sortedTickets = new ArrayList<>(unsortedTickets);
        sortedTickets.sort((t1, t2) -> {
            Integer boxed = t1.getValue().get(0).getId();
            return boxed.compareTo(t2.getValue().get(0).getId());
        });

        ImmutableMap.copyOf(sortedTickets).forEach((uuid, tickets) -> {
            //noinspection ConstantConditions
            if (onlineOnly && !this.userManager.isOnline(uuid)) {
                return;
            }

            soul.message(Messages.GENERAL__LIST_HEADER_FORMAT, "name", this.userManager.getName(uuid));

            tickets.forEach(ticket -> {
                String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
                soul.message(Messages.GENERAL__LIST_FORMAT, replacements);
            });
        });
    }

    private void processStatus(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        UUID target = c.getOrDefault("target", null);

        if (target != null) {
            soul.message(Messages.TITLES__SPECIFIC_TICKETS, "player", this.userManager.getName(target));
        } else {
            soul.message(Messages.TITLES__TICKET_STATUS);
        }

        TicketStats data;

        if (target != null) {
            data = this.ticketManager.getStats(target);
        } else {
            data = this.ticketManager.getStats();
        }

        data.forEach((status, amount) -> {
            if (amount != 0) {
                soul.message(amount.toString() + " " + status.name().toLowerCase());
            }
        });
    }

    private void processHighscore(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        TimeAmount amount = c.get("amount");

        Map<UUID, Integer> highscores = this.ticketManager.getHighscores(amount);
        soul.message(Messages.TITLES__HIGHSCORES);

        highscores.forEach((uuid, number) ->
                soul.message(
                        Messages.GENERAL__HS_FORMAT,
                        "target", this.userManager.getName(uuid),
                        "amount", number.toString()
                )
        );
    }

}
