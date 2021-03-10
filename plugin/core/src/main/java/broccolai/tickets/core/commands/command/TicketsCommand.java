package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.user.OnlineSoul;
import cloud.commandframework.CommandManager;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketsCommand extends CommonCommands {
//
//    private final Config config;
//    private final TicketsEventBus eventBus;
//    private final UserManager<?, ?, ?> userManager;
//    private final TicketManager ticketManager;
//
//    public TicketsCommand(
//            final @NonNull Config config,
//            final @NonNull TicketsEventBus eventBus,
//            final @NonNull UserManager<?, ?, ?> userManager,
//            final @NonNull TicketManager ticketManager
//    ) {
//        this.config = config;
//        this.eventBus = eventBus;
//        this.userManager = userManager;
//        this.ticketManager = ticketManager;
//    }

    @Override
    public void register(
            @NonNull final CommandManager<OnlineSoul> manager
    ) {
//        final Command.Builder<OnlineSoul> builder = manager.commandBuilder("tickets", "tis");
//
//        manager.command(builder.literal(
//                config.getAliasShow().getFirst(),
//                ArgumentDescription.of("Show a ticket"),
//                config.getAliasShow().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".show")
//                .argument(TargetArgument.of("target"))
//                .argument(TicketArgument.of(false, false))
//                .handler(c -> this.processShow(c.getSender(), c.get("ticket"))));
//
//        manager.command(builder.literal(
//                config.getAliasClaim().getFirst(),
//                ArgumentDescription.of("Claim a ticket"),
//                config.getAliasClaim().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".pick")
//                .argument(TargetArgument.of("target"))
//                .argument(TicketArgument.of(false, false, TicketStatus.OPEN))
//                .handler(this::processClaim)
//                .build());
//
//        manager.command(builder.literal(
//                config.getAliasAssign().getFirst(),
//                ArgumentDescription.of("Assign a ticket"),
//                config.getAliasAssign().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".assign")
//                .argument(TargetArgument.of("staff"))
//                .argument(TargetArgument.of("target"))
//                .argument(TicketArgument.of(false, false, TicketStatus.OPEN))
//                .handler(this::processAssign)
//                .build());
//
//        manager.command(builder.literal(
//                config.getAliasDone().getFirst(),
//                ArgumentDescription.of("Complete a ticket"),
//                config.getAliasDone().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".done")
//                .argument(TargetArgument.of("target"))
//                .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED))
//                .handler(this::processDone)
//                .build());
//
//        manager.command(builder.literal(
//                config.getAliasUnclaim().getFirst(),
//                ArgumentDescription.of("Unclaim a ticket"),
//                config.getAliasUnclaim().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".yield")
//                .argument(TargetArgument.of("target"))
//                .argument(TicketArgument.of(false, false, TicketStatus.PICKED))
//                .handler(this::processUnclaim)
//                .build());
//
//        manager.command(builder.literal(
//                config.getAliasNote().getFirst(),
//                ArgumentDescription.of("Add a note to a ticket"),
//                config.getAliasNote().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".note")
//                .argument(TargetArgument.of("target"))
//                .argument(TicketArgument.of(true, false))
//                .argument(StringArgument.of("message"))
//                .handler(this::processNote)
//                .build());
//
//        manager.command(builder.literal(
//                config.getAliasReopen().getFirst(),
//                ArgumentDescription.of("Reopen a ticket"),
//                config.getAliasReopen().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".reopen")
//                .argument(TargetArgument.of("target"))
//                .argument(TicketArgument.of(false, false, TicketStatus.CLOSED))
//                .handler(this::processReopen)
//                .build());
//
//        manager.command(builder.literal(
//                config.getAliasTeleport().getFirst(),
//                ArgumentDescription.of("Teleport to a tickets creation location"),
//                config.getAliasTeleport().getSecond()
//        )
//                .senderType(PlayerSoul.class)
//                .permission(Constants.STAFF_PERMISSION + ".teleport")
//                .argument(TargetArgument.of("target"))
//                .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED, TicketStatus.CLOSED))
//                .handler(this::processTeleport)
//                .build());
//
//        manager.command(builder.literal(
//                config.getAliasLog().getFirst(),
//                ArgumentDescription.of("View a tickets log"),
//                config.getAliasLog().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".log")
//                .argument(TargetArgument.of("target"))
//                .argument(TicketArgument.of(false, false, TicketStatus.OPEN, TicketStatus.PICKED, TicketStatus.CLOSED))
//                .handler(c -> processLog(c.getSender(), c.get("ticket")))
//                .build());
//
//        manager.command(builder.literal(
//                config.getAliasList().getFirst(),
//                ArgumentDescription.of("List tickets"),
//                config.getAliasList().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".list")
//                .flag(manager.flagBuilder("status")
//                        .withArgument(EnumArgument.of(TicketStatus.class, "status")))
//                .flag(manager.flagBuilder("player")
//                        .withArgument(TargetArgument.of("player")))
//                .flag(manager.flagBuilder("onlineOnly")
//                        .withArgument(BooleanArgument.of("onlineOnly")))
//                .handler(this::processList));
//
//        manager.command(builder.literal(
//                config.getAliasStatus().getFirst(),
//                ArgumentDescription.of("View amount of tickets in"),
//                config.getAliasStatus().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".status")
//                .argument(TargetArgument.of("target"))
//                .handler(this::processStatus)
//                .build());
//
//        manager.command(builder.literal(
//                config.getAliasHighscore().getFirst(),
//                ArgumentDescription.of("View highscores of ticket completions"),
//                config.getAliasHighscore().getSecond()
//        )
//                .permission(Constants.STAFF_PERMISSION + ".highscore")
//                .argument(EnumArgument.optional(TimeAmount.class, "amount"))
//                .handler(this::processHighscore)
//                .build());
    }
//
//    private void processClaim(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        OnlineSoul sender = c.getSender();
//        Ticket ticket = c.get("ticket");
//
//        ticketManager.insertMessage(ticket.getId(), ticket.pick(sender.uuid()));
//        this.eventBus.post(new NotificationEvent(NotificationReason.CLAIM_TICKET, sender, ticket.getPlayerUniqueID(), ticket));
//        ticketManager.updateTicket(ticket);
//    }
//
//    private void processAssign(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        OnlineSoul sender = c.getSender();
//        Ticket ticket = c.get("ticket");
//        UUID staff = c.get("staff");
//
//        ticketManager.insertMessage(ticket.getId(), ticket.pick(staff));
//        this.eventBus.post(new NotificationEvent(NotificationReason.ASSIGN_TICKET, sender, staff, ticket));
//        ticketManager.updateTicket(ticket);
//    }
//
//    private void processDone(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        OnlineSoul sender = c.getSender();
//        Ticket ticket = c.get("ticket");
//
//        ticketManager.insertMessage(ticket.getId(), ticket.done(sender.uuid()));
//        this.eventBus.post(new NotificationEvent(NotificationReason.DONE_TICKET, sender, ticket.getPlayerUniqueID(), ticket));
//        ticketManager.updateTicket(ticket);
//    }
//
//    private void processUnclaim(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        OnlineSoul sender = c.getSender();
//        Ticket ticket = c.get("ticket");
//
//        ticketManager.insertMessage(ticket.getId(), ticket.yield(sender.uuid()));
//        this.eventBus.post(new NotificationEvent(NotificationReason.UNCLAIM_TICKET, sender, ticket.getPlayerUniqueID(),
//                ticket
//        ));
//        ticketManager.updateTicket(ticket);
//    }
//
//    private void processNote(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        OnlineSoul sender = c.getSender();
//        Ticket ticket = c.get("ticket");
//
//        ticketManager.insertMessage(ticket.getId(), ticket.note(sender.uuid(), c.get("message")));
//        this.eventBus.post(new NotificationEvent(NotificationReason.NOTE_TICKET, sender, ticket.getPlayerUniqueID(), ticket));
//        ticketManager.updateTicket(ticket);
//    }
//
//    private void processReopen(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        OnlineSoul sender = c.getSender();
//        Ticket ticket = c.get("ticket");
//
//        ticketManager.insertMessage(ticket.getId(), ticket.reopen(sender.uuid()));
//        this.eventBus.post(new NotificationEvent(NotificationReason.REOPEN_TICKET, sender, ticket.getPlayerUniqueID(), ticket));
//        ticketManager.updateTicket(ticket);
//    }
//
//    private void processTeleport(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        PlayerSoul soul = (PlayerSoul) c.getSender();
//        Ticket ticket = c.get("ticket");
//
//        this.eventBus.post(new NotificationEvent(NotificationReason.TELEPORT_TICKET, soul, ticket.getPlayerUniqueID(), ticket));
//        soul.teleport(null); //todo
//    }
//
//    private void processList(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        final OnlineSoul sender = c.getSender();
//        final User player = c.flags().getValue("player", null);
//        final Boolean onlineOnly = c.flags().getValue("onlineOnly", false);
//        final TicketStatus[] statuses = TicketStatus.fromFlags(c.flags());
//
//        if (player != null) {
//            // todo
//            Template template = Template.of("player", player.getName());
//            Component title = Message.TITLE__SPECIFIC_TICKETS.use(template);
//            sender.sendMessage(title);
//
//            this.ticketManager.getTickets(player.getUniqueId(), statuses).forEach(ticket -> {
//                Component list = Message.FORMAT__LIST.use(ticket.templates());
//                sender.sendMessage(list);
//            });
//
//            return;
//        }
//
//        Component title = Message.TITLE__ALL_TICKETS.use();
//        sender.sendMessage(title);
//
//        // todo: ugly
//        Set<Map.Entry<UUID, List<Ticket>>> unsortedTickets = Lists
//                .group(this.ticketManager.getTickets(statuses), Ticket::getPlayerUniqueID)
//                .entrySet();
//        List<Map.Entry<UUID, List<Ticket>>> sortedTickets = new ArrayList<>(unsortedTickets);
//        sortedTickets.sort((t1, t2) -> {
//            Integer boxed = t1.getValue().get(0).getId();
//            return boxed.compareTo(t2.getValue().get(0).getId());
//        });
//
//        ImmutableMap.copyOf(sortedTickets).forEach((uuid, tickets) -> {
//            //noinspection ConstantConditions
//            if (onlineOnly && !this.userManager.isOnline(uuid)) {
//                return;
//            }
//
//            // todo
//            Template template = Template.of("player", this.userManager.getName(uuid));
//            Component listHeader = Message.FORMAT__LIST_HEADER.use(template);
//            sender.sendMessage(listHeader);
//
//            tickets.forEach(ticket -> {
//                Component list = Message.FORMAT__LIST.use(ticket.templates());
//                sender.sendMessage(list);
//            });
//        });
//    }
//
//    private void processStatus(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        OnlineSoul sender = c.getSender();
//        User target = c.getOrDefault("target", null);
//
//        TextComponent.Builder builder = Component.text();
//        TicketStats data;
//        if (target != null) {
//            // todo
//            Template playerTemplate = Template.of("player", target.getName());
//            Component title = Message.TITLE__SPECIFIC_TICKETS.use(playerTemplate);
//
//            data = this.ticketManager.getStats(target.getUniqueId());
//            builder.append(title);
//        } else {
//            Component title = Message.TITLE__TICKET_STATUS.use();
//
//            data = this.ticketManager.getStats();
//            builder.append(title);
//        }
//
//        Message key = Message.FORMAT__STATUS;
//        data.forEach((status, amount) -> {
//            if (amount != 0) {
//                Component component = key.use(
//                        Template.of("amount", String.valueOf(amount)),
//                        Template.of("status", status.name().toLowerCase())
//                );
//
//                builder.append(Component.empty(), component);
//            }
//        });
//
//        sender.sendMessage(builder);
//    }
//
//    private void processHighscore(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        OnlineSoul sender = c.getSender();
//        TimeAmount amount = c.<TimeAmount>getOptional("amount").orElse(TimeAmount.FOREVER);
//
//        Map<UUID, Integer> highscores = this.ticketManager.getHighscores(amount);
//
//        Component title = Message.TITLE__HIGHSCORES.use();
//        sender.sendMessage(title);
//
//        highscores.forEach((uuid, number) -> {
//            Component component = Message.FORMAT__HS.use(
//                    Template.of("target", this.userManager.getName(uuid)),
//                    Template.of("amount", number.toString())
//            );
//            sender.sendMessage(component);
//        });
//    }


}
