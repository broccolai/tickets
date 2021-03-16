package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.interactions.InteractionService;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.core.commands.arguments.TicketParserMode;
import broccolai.tickets.core.configuration.CommandsConfiguration;
import broccolai.tickets.core.factory.CloudArgumentFactory;
import broccolai.tickets.core.utilities.Constants;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketsCommand extends CommonCommands {

    private final CommandsConfiguration.TicketsConfiguration config;
    private final CloudArgumentFactory argumentFactory;
    private final InteractionService interactionService;

    @Inject
    public TicketsCommand(
            final CommandsConfiguration.@NonNull TicketsConfiguration config,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull MessageService messageService,
            final @NonNull InteractionService interactionService
    ) {
        super(messageService);
        this.config = config;
        this.argumentFactory = argumentFactory;
        this.interactionService = interactionService;
    }

    @Override
    public void register(
            final @NonNull CommandManager<OnlineSoul> manager
    ) {
        final Command.Builder<OnlineSoul> builder = manager.commandBuilder("tickets", "tis");

        manager.command(builder.literal(
                this.config.show.main,
                ArgumentDescription.of("Show a ticket"),
                this.config.show.aliases
        )
                .permission(Constants.STAFF_PERMISSION + ".show")
                .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY))
                .handler(c -> this.processShow(c.getSender(), c.get("ticket")))
        );

        manager.command(builder.literal(
                this.config.claim.main,
                ArgumentDescription.of("Claim a ticket"),
                this.config.claim.aliases
        )
                .permission(Constants.STAFF_PERMISSION + ".claim")
                .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY))
                .handler(this::processClaim)
                .build()
        );

        manager.command(builder.literal(
                this.config.assign.main,
                ArgumentDescription.of("Assign a ticket"),
                this.config.assign.aliases
        )
                .permission(Constants.STAFF_PERMISSION + ".assign")
                .argument(this.argumentFactory.target("target"))
                .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY))
                .handler(this::processAssign)
                .build()
        );

        manager.command(builder.literal(
                this.config.complete.main,
                ArgumentDescription.of("Complete a ticket"),
                this.config.complete.aliases
        )
                .permission(Constants.STAFF_PERMISSION + ".complete")
                .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY))
                .handler(this::processComplete)
                .build()
        );

        manager.command(builder.literal(
                this.config.unclaim.main,
                ArgumentDescription.of("Unclaim a ticket"),
                this.config.unclaim.aliases
        )
                .permission(Constants.STAFF_PERMISSION + ".yield")
                .argument(this.argumentFactory.ticket("target", TicketParserMode.ANY))
                .handler(this::processUnclaim)
                .build()
        );

        manager.command(builder.literal(
                this.config.note.main,
                ArgumentDescription.of("Add a note to a ticket"),
                this.config.note.aliases
        )
                .permission(Constants.STAFF_PERMISSION + ".note")
                .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY))
                .argument(StringArgument.of("message"))
                .handler(this::processNote)
                .build()
        );

        manager.command(builder.literal(
                this.config.reopen.main,
                ArgumentDescription.of("Reopen a ticket"),
                this.config.reopen.aliases
        )
                .permission(Constants.STAFF_PERMISSION + ".reopen")
                .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY))
                .handler(this::processReopen)
                .build()
        );

        manager.command(builder.literal(
                this.config.teleport.main,
                ArgumentDescription.of("Teleport to a tickets creation location"),
                this.config.teleport.aliases
        )
                .senderType(PlayerSoul.class)
                .permission(Constants.STAFF_PERMISSION + ".teleport")
                .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY))
                .handler(this::processTeleport)
                .build()
        );

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

    private void processClaim(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
        OnlineSoul sender = c.getSender();
        Ticket ticket = c.get("ticket");

        this.interactionService.claim(sender, ticket);
    }

    private void processAssign(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
        OnlineSoul sender = c.getSender();
        Ticket ticket = c.get("ticket");
        Soul target = c.get("target");

        this.interactionService.assign(sender, target, ticket);
    }

    private void processComplete(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
        OnlineSoul sender = c.getSender();
        Ticket ticket = c.get("ticket");

        this.interactionService.complete(sender, ticket);
    }

    private void processUnclaim(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
        OnlineSoul sender = c.getSender();
        Ticket ticket = c.get("ticket");

        this.interactionService.unclaim(sender, ticket);
    }

    private void processNote(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
        OnlineSoul sender = c.getSender();
        Ticket ticket = c.get("ticket");

        this.interactionService.unclaim(sender, ticket);
    }

    private void processReopen(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
        OnlineSoul sender = c.getSender();
        Ticket ticket = c.get("ticket");

        this.interactionService.reopen(sender, ticket);
    }

    private void processTeleport(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
        PlayerSoul soul = (PlayerSoul) c.getSender();
        Ticket ticket = c.get("ticket");

        soul.teleport(ticket.position());
    }
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
