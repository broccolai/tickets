package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.interaction.Action;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.OnlineUser;
import broccolai.tickets.api.model.user.User;
import broccolai.tickets.api.service.modification.ModificationService;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.core.commands.arguments.TicketParserMode;
import broccolai.tickets.core.configuration.CommandsConfiguration;
import broccolai.tickets.core.configuration.MainConfiguration;
import broccolai.tickets.core.factory.CloudArgumentFactory;
import broccolai.tickets.core.model.interaction.BasicMessageInteraction;
import broccolai.tickets.core.utilities.Constants;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketsCommand extends CommonCommands {

    private final CommandsConfiguration.TicketsConfiguration config;
    private final CloudArgumentFactory argumentFactory;
    private final MessageService messageService;
    private final TicketService ticketService;
    private final ModificationService modificationService;
    private final UserService userService;

    @Inject
    public TicketsCommand(
            final @NonNull MainConfiguration config,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull MessageService messageService,
            final @NonNull StorageService storageService,
            final @NonNull TicketService ticketService,
            final @NonNull ModificationService modificationService,
            final @NonNull UserService userService
    ) {
        super(messageService, storageService, userService);
        this.config = config.commandsConfiguration.tickets;
        this.argumentFactory = argumentFactory;
        this.messageService = messageService;
        this.ticketService = ticketService;
        this.modificationService = modificationService;
        this.userService = userService;
    }

    @Override
    public void register(
            final @NonNull CommandManager<OnlineUser> manager
    ) {
        final Command.Builder<OnlineUser> builder = manager.commandBuilder("tickets", "tis");

        manager.command(builder.literal(
                this.config.show.main,
                ArgumentDescription.of("Show a ticket"),
                this.config.show.aliases
                )
                        .permission(Constants.STAFF_PERMISSION + ".show")
                        .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY, EnumSet.allOf(TicketStatus.class), 0))
                        .handler(c -> this.processShow(c.getSender(), c.get("ticket")))
        );

        manager.command(builder.literal(
                this.config.claim.main,
                ArgumentDescription.of("Claim a ticket"),
                this.config.claim.aliases
                )
                        .permission(Constants.STAFF_PERMISSION + ".claim")
                        .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY, EnumSet.of(TicketStatus.OPEN), 0))
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
                        .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY, EnumSet.of(TicketStatus.OPEN), 1))
                        .handler(this::processAssign)
                        .build()
        );

        manager.command(builder.literal(
                this.config.complete.main,
                ArgumentDescription.of("Complete a ticket"),
                this.config.complete.aliases
                )
                        .permission(Constants.STAFF_PERMISSION + ".complete")
                        .argument(this.argumentFactory.ticket(
                                "ticket",
                                TicketParserMode.ANY,
                                EnumSet.of(TicketStatus.CLAIMED),
                                0
                        ))
                        .handler(this::processComplete)
                        .build()
        );

        manager.command(builder.literal(
                this.config.unclaim.main,
                ArgumentDescription.of("Unclaim a ticket"),
                this.config.unclaim.aliases
                )
                        .permission(Constants.STAFF_PERMISSION + ".yield")
                        .argument(this.argumentFactory.ticket(
                                "ticket",
                                TicketParserMode.ANY,
                                EnumSet.of(TicketStatus.OPEN, TicketStatus.CLAIMED),
                                0
                        ))
                        .handler(this::processUnclaim)
                        .build()
        );

        manager.command(builder.literal(
                this.config.note.main,
                ArgumentDescription.of("Add a note to a ticket"),
                this.config.note.aliases
                )
                        .permission(Constants.STAFF_PERMISSION + ".note")
                        .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY, EnumSet.allOf(TicketStatus.class), 0))
                        .argument(StringArgument.of("message", StringArgument.StringMode.GREEDY))
                        .handler(this::processNote)
                        .build()
        );

        manager.command(builder.literal(
                this.config.reopen.main,
                ArgumentDescription.of("Reopen a ticket"),
                this.config.reopen.aliases
                )
                        .permission(Constants.STAFF_PERMISSION + ".reopen")
                        .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY, EnumSet.of(TicketStatus.CLOSED), 0))
                        .handler(this::processReopen)
                        .build()
        );

        manager.command(builder.literal(
                this.config.log.main,
                ArgumentDescription.of("View a tickets log"),
                this.config.log.aliases
                )
                        .permission(Constants.STAFF_PERMISSION + ".log")
                        .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY, EnumSet.allOf(TicketStatus.class), 0))
                        .handler(c -> processLog(c.getSender(), c.get("ticket")))
                        .build()
        );

        manager.command(builder.literal(
                this.config.list.main,
                ArgumentDescription.of("List tickets"),
                this.config.list.aliases
                )
                        .permission(Constants.STAFF_PERMISSION + ".list")
                        .flag(manager.flagBuilder("status")
                                .withArgument(EnumArgument.of(TicketStatus.class, "status"))
                        )
                        .flag(manager.flagBuilder("uuid")
                                .withArgument(this.argumentFactory.target("target"))
                        )
                        .handler(this::processList)
        );

        //todo: USE CONFIG
        manager.command(builder.literal(
                "context",
                ArgumentDescription.of("View a tickets context")
                )
                .permission(Constants.STAFF_PERMISSION + ".context")
                .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY, EnumSet.allOf(TicketStatus.class), 0))
                .handler(this::processContext)
        );

    }

    private void processClaim(final @NonNull CommandContext<@NonNull OnlineUser> c) {
        OnlineUser sender = c.getSender();
        Ticket ticket = c.get("ticket");

        this.modificationService.claim(sender, ticket);
    }

    private void processAssign(final @NonNull CommandContext<@NonNull OnlineUser> c) {
        OnlineUser sender = c.getSender();
        Ticket ticket = c.get("ticket");
        User target = c.get("target");

        this.modificationService.assign(sender, target, ticket);
    }

    private void processComplete(final @NonNull CommandContext<@NonNull OnlineUser> c) {
        OnlineUser sender = c.getSender();
        Ticket ticket = c.get("ticket");

        this.modificationService.complete(sender, ticket);
    }

    private void processUnclaim(final @NonNull CommandContext<@NonNull OnlineUser> c) {
        OnlineUser sender = c.getSender();
        Ticket ticket = c.get("ticket");

        this.modificationService.unclaim(sender, ticket);
    }

    private void processNote(final @NonNull CommandContext<@NonNull OnlineUser> c) {
        OnlineUser sender = c.getSender();
        Ticket ticket = c.get("ticket");
        String message = c.get("message");
        MessageInteraction interaction = new BasicMessageInteraction(
                Action.NOTE,
                LocalDateTime.now(ZoneId.systemDefault()),
                sender.uuid(),
                message
        );

        this.modificationService.note(sender, ticket, interaction);
    }

    private void processReopen(final @NonNull CommandContext<@NonNull OnlineUser> c) {
        OnlineUser sender = c.getSender();
        Ticket ticket = c.get("ticket");

        this.modificationService.reopen(sender, ticket);
    }

    private void processList(final @NonNull CommandContext<@NonNull OnlineUser> c) {
        OnlineUser soul = c.getSender();
        Optional<User> target = c.flags().getValue("uuid");
        Set<TicketStatus> statuses = TicketStatus.from(c.flags());

        Map<UUID, Collection<Ticket>> tickets = target.map(value -> {
            return Collections.singletonMap(value.uuid(), this.ticketService.get(
                    value,
                    statuses
            ));
        }).orElse(this.ticketService.get(statuses).asMap());

        List<Component> results = new ArrayList<>();
        results.add(Component.empty());
        results.add(this.messageService.listTitleAll());

        tickets.forEach((uuid, playersTickets) -> {
            User player = this.userService.snapshot(uuid);
            results.add(this.messageService.listTitleHeader(player));

            for (final Ticket ticket : playersTickets) {
                results.add(this.messageService.listTitleEntry(ticket));
            }
        });

        soul.sendMessage(Component.join(
                Component.newline(),
                results
        ));
    }

    private void processContext(final @NonNull CommandContext<@NonNull OnlineUser> c) {
        OnlineUser soul = c.getSender();
        Ticket ticket = c.get("ticket");

        ticket.context().forEach((key, value) -> {
            soul.sendMessage(Component.text(key.name() + ":" + value.toString()));
        });
    }

}
