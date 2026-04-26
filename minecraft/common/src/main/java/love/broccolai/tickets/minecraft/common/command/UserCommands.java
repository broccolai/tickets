package love.broccolai.tickets.minecraft.common.command;

import com.google.inject.Inject;
import java.util.EnumSet;
import java.util.Set;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.format.TicketFormData;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.format.TicketFormatPart;
import love.broccolai.tickets.common.configuration.TicketsConfiguration;
import love.broccolai.tickets.minecraft.common.TicketPermissions;
import love.broccolai.tickets.minecraft.common.factory.CommandArgumentFactory;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.model.PlayerCommander;
import love.broccolai.tickets.minecraft.common.parsers.LocationDescriptor;
import love.broccolai.tickets.minecraft.common.service.TicketOperations;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.parser.flag.CommandFlag;
import org.incendo.cloud.parser.standard.EnumParser;
import org.incendo.cloud.parser.standard.StringParser;
import org.jspecify.annotations.NullMarked;

import static love.broccolai.tickets.api.model.TicketStatus.OPEN;
import static love.broccolai.tickets.api.model.TicketStatus.PICKED;

@NullMarked
public final class UserCommands extends AbstractCommand {

    private static final CloudKey<Ticket> TICKET_KEY = CloudKey.cloudKey("ticket", Ticket.class);
    private static final CloudKey<String> MESSAGE_KEY = CloudKey.cloudKey("message", String.class);

    private final TicketsConfiguration ticketsConfiguration;
    private final TicketOperations actions;

    private final CommandArgumentFactory commandArgumentFactory;
    private final LocationDescriptor locationDescriptor;

    @Inject
    public UserCommands(
        final TicketsConfiguration ticketsConfiguration,
        final TicketOperations actions,
        final CommandArgumentFactory commandArgumentFactory,
        final LocationDescriptor locationDescriptor
    ) {
        this.ticketsConfiguration = ticketsConfiguration;
        this.actions = actions;
        this.commandArgumentFactory = commandArgumentFactory;
        this.locationDescriptor = locationDescriptor;
    }

    @Override
    public void register(final CommandManager<Commander> commandManager) {
        Command.Builder<PlayerCommander> root = commandManager
            .commandBuilder("ticket", "ti")
            .senderType(PlayerCommander.class);

        Command.Builder<PlayerCommander> createBase = root.literal("create");

        for (TicketFormat format : this.ticketsConfiguration.types) {
            Command.Builder<PlayerCommander> command = this.createSubCommand(createBase, format);

            commandManager.command(
                command.handler(context -> this.handleCreate(context, format))
            );
        }

        commandManager.command(
            root.literal("show")
                .permission(TicketPermissions.USER_SHOW)
                .required(TICKET_KEY, this.commandArgumentFactory.selfTicket(EnumSet.allOf(TicketStatus.class)))
                .handler(this::handleShow)
        );

        commandManager.command(
            root.literal("log")
                .permission(TicketPermissions.USER_SHOW)
                .required(TICKET_KEY, this.commandArgumentFactory.selfTicket(EnumSet.allOf(TicketStatus.class)))
                .handler(this::handleShow)
        );

        commandManager.command(
            root.literal("list")
                .permission(TicketPermissions.USER_LIST)
                .flag(this.statusFlag())
                .handler(this::handleList)
        );

        commandManager.command(
            root.literal("comment", "update")
                .permission(TicketPermissions.USER_COMMENT)
                .required(TICKET_KEY, this.commandArgumentFactory.selfTicket(EnumSet.of(OPEN, PICKED)))
                .required(MESSAGE_KEY, StringParser.greedyStringParser())
                .handler(this::handleComment)
        );

        commandManager.command(
            root.literal("close")
                .permission(TicketPermissions.USER_CLOSE)
                .required(TICKET_KEY, this.commandArgumentFactory.selfTicket(EnumSet.of(OPEN, PICKED)))
                .handler(this::handleClose)
        );
    }

    private Command.Builder<PlayerCommander> createSubCommand(
        final Command.Builder<PlayerCommander> base,
        final TicketFormat format
    ) {
        Command.Builder<PlayerCommander> command = base.literal(format.identifier())
            .permission(TicketPermissions.USER_CREATE);

        for (TicketFormatPart part : format.parts()) {
            ParserDescriptor<Commander, ?> parser = switch (part.style()) {
                case Player -> this.commandArgumentFactory.profile();
                case Sentence -> StringParser.greedyStringParser();
                case Location -> this.locationDescriptor;
            };

            command = command.required(part.identifier(), parser);
        }

        return command;
    }

    private void handleCreate(final CommandContext<PlayerCommander> context, final TicketFormat format) {
        PlayerCommander commander = context.sender();
        TicketFormData form = TicketFormData.empty(format.identifier());

        for (TicketFormatPart part : format.parts()) {
            CloudKey<?> key = CloudKey.cloudKey(part.identifier(), part.style().contentType());
            form = form.with(part.identifier(), context.get(key));
        }

        this.actions.create(commander, format, form);
    }

    private void handleList(final CommandContext<PlayerCommander> context) {
        this.actions.listUser(context.sender(), this.statuses(context));
    }

    private void handleShow(final CommandContext<PlayerCommander> context) {
        this.actions.show(context.sender(), context.get(TICKET_KEY));
    }

    private void handleComment(final CommandContext<PlayerCommander> context) {
        this.actions.commentUser(context.sender(), context.get(TICKET_KEY), context.get(MESSAGE_KEY));
    }

    private void handleClose(final CommandContext<PlayerCommander> context) {
        this.actions.closeUser(context.sender(), context.get(TICKET_KEY));
    }

    private CommandFlag<TicketStatus> statusFlag() {
        return CommandFlag.<PlayerCommander>builder("status")
            .withComponent(EnumParser.enumParser(TicketStatus.class))
            .build();
    }

    private Set<TicketStatus> statuses(final CommandContext<PlayerCommander> context) {
        TicketStatus status = context.flags().getValue("status", null);

        if (status != null) {
            return EnumSet.of(status);
        }

        return EnumSet.of(OPEN, PICKED);
    }
}
