package broccolai.tickets.core.commands.command;

import broccolai.corn.core.Lists;
import broccolai.tickets.core.commands.arguments.TargetArgument;
import broccolai.tickets.core.commands.arguments.TicketArgument;
import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.events.api.NotificationEvent;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.interactions.NotificationReason;
import broccolai.tickets.core.locale.Message;
import broccolai.tickets.core.storage.TimeAmount;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.ticket.TicketManager;
import broccolai.tickets.core.ticket.TicketStats;
import broccolai.tickets.core.ticket.TicketStatus;
import broccolai.tickets.core.user.PlayerSoul;
import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.UserManager;
import broccolai.tickets.core.utilities.Constants;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.Description;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.google.common.collect.ImmutableMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Template;
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
                config.getAliasClaim().getFirst(),
                Description.of("Claim a ticket"),
                config.getAliasClaim().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".pick")
                .argument(TargetArgument.of("target"))
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN))
                .handler(this::processClaim)
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
                Description.of("Complete a ticket"),
                config.getAliasDone().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".done")
                .argument(TargetArgument.of("target"))
                .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED))
                .handler(this::processDone)
                .build());

        manager.command(builder.literal(
                config.getAliasUnclaim().getFirst(),
                Description.of("Unclaim a ticket"),
                config.getAliasUnclaim().getSecond()
        )
                .permission(Constants.STAFF_PERMISSION + ".yield")
                .argument(TargetArgument.of("target"))
                .argument(TicketArgument.of(false, false, TicketStatus.PICKED))
                .handler(this::processUnclaim)
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

    private void processClaim(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        ticket.pick(soul.getUniqueId());
        this.eventBus.post(new NotificationEvent(NotificationReason.CLAIM_TICKET, soul, ticket.getPlayerUniqueID(), ticket));
    }

    private void processAssign(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");
        UUID staff = c.get("staff");

        ticket.pick(staff);
        this.eventBus.post(new NotificationEvent(NotificationReason.ASSIGN_TICKET, soul, staff, ticket));
    }

    private void processDone(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        ticket.done(soul.getUniqueId());
        this.eventBus.post(new NotificationEvent(NotificationReason.DONE_TICKET, soul, ticket.getPlayerUniqueID(), ticket));
    }

    private void processUnclaim(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        ticket.yield(soul.getUniqueId());
        this.eventBus.post(new NotificationEvent(NotificationReason.UNCLAIM_TICKET, soul, ticket.getPlayerUniqueID(),
                ticket
        ));
    }

    private void processNote(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        ticket.note(soul.getUniqueId(), c.get("message"));
        this.eventBus.post(new NotificationEvent(NotificationReason.NOTE_TICKET, soul, ticket.getPlayerUniqueID(), ticket));
    }

    private void processReopen(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        Ticket ticket = c.get("ticket");

        ticket.reopen(soul.getUniqueId());
        this.eventBus.post(new NotificationEvent(NotificationReason.REOPEN_TICKET, soul, ticket.getPlayerUniqueID(), ticket));
    }

    private void processTeleport(final @NonNull CommandContext<Soul<C>> c) {
        PlayerSoul<C, ?> soul = (PlayerSoul<C, ?>) c.getSender();
        Ticket ticket = c.get("ticket");

        this.eventBus.post(new NotificationEvent(NotificationReason.TELEPORT_TICKET, soul, ticket.getPlayerUniqueID(), ticket));
        soul.teleport(ticket.getLocation());
    }

    private void processList(final @NonNull CommandContext<Soul<C>> c) {
        final Soul<C> soul = c.getSender();
        final UUID player = c.flags().getValue("player", null);
        final Boolean onlineOnly = c.flags().getValue("onlineOnly", false);
        final TicketStatus[] statuses = this.statusesFromFlags(c.flags());

        if (player != null) {
            // todo
            Template template = Template.of("player", this.userManager.getName(player));
            Component title = Message.TITLE__SPECIFIC_TICKETS.use(template);
            TextComponent.Builder builder = Component.text()
                    .append(title);

            this.ticketManager.getTickets(player, statuses).forEach(ticket -> {
                Component list = Message.FORMAT__LIST.use(ticket.templates());
                builder.append(Component.newline(), list);
            });

            soul.sendMessage(builder);
            return;
        }

        Component title = Message.TITLE__ALL_TICKETS.use();
        TextComponent.Builder builder = Component.text()
                .append(title);

        // todo: ugly
        Set<Map.Entry<UUID, List<Ticket>>> unsortedTickets = Lists
                .group(this.ticketManager.getTickets(statuses), Ticket::getPlayerUniqueID)
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

            // todo
            Template template = Template.of("player", this.userManager.getName(uuid));
            Component listHeader = Message.FORMAT__LIST_HEADER.use(template);
            builder.append(Component.newline(), listHeader);

            tickets.forEach(ticket -> {
                Component list = Message.FORMAT__LIST.use(ticket.templates());
                builder.append(Component.newline(), list);
            });
        });

        soul.sendMessage(builder);
    }

    private void processStatus(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        UUID target = c.getOrDefault("target", null);

        TextComponent.Builder builder = Component.text();
        TicketStats data;
        if (target != null) {
            // todo
            Template playerTemplate = Template.of("player", this.userManager.getName(target));
            Component title = Message.TITLE__SPECIFIC_TICKETS.use(playerTemplate);

            data = this.ticketManager.getStats(target);
            builder.append(title);
        } else {
            Component title = Message.TITLE__TICKET_STATUS.use();

            data = this.ticketManager.getStats();
            builder.append(title);
        }

        Message key = Message.FORMAT__STATUS;
        data.forEach((status, amount) -> {
            if (amount != 0) {
                Component component = key.use(
                        Template.of("amount", String.valueOf(amount)),
                        Template.of("status", status.name().toLowerCase())
                );

                builder.append(Component.empty(), component);
            }
        });

        soul.sendMessage(builder);
    }

    private void processHighscore(final @NonNull CommandContext<Soul<C>> c) {
        Soul<C> soul = c.getSender();
        TimeAmount amount = c.get("amount");

        Map<UUID, Integer> highscores = this.ticketManager.getHighscores(amount);

        Component title = Message.TITLE__HIGHSCORES.use();
        TextComponent.Builder builder = Component.text()
                .append(title);

        highscores.forEach((uuid, number) -> {
            Component component = Message.FORMAT__HS.use(
                    Template.of("target", this.userManager.getName(uuid)),
                    Template.of("amount", number.toString())
            );
            builder.append(component);
        });

        soul.sendMessage(builder);
    }

}
