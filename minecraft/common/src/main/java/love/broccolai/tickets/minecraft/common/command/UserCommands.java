package love.broccolai.tickets.minecraft.common.command;

import com.google.inject.Inject;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.format.TicketFormatContent;
import love.broccolai.tickets.api.model.format.TicketFormatPart;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.configuration.TicketsConfiguration;
import love.broccolai.tickets.minecraft.common.factory.CommandArgumentFactory;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.model.PlayerCommander;
import love.broccolai.tickets.minecraft.common.parsers.LabeledDescriptor;
import love.broccolai.tickets.minecraft.common.parsers.LocationDescriptor;
import love.broccolai.tickets.minecraft.common.parsers.ticket.TicketTypeDescriptor;
import love.broccolai.tickets.minecraft.common.service.MessageService;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.ParserDescriptor;
import org.jspecify.annotations.NullMarked;

import java.util.Comparator;
import java.util.EnumSet;

import static net.kyori.adventure.text.Component.text;

@NullMarked
public final class UserCommands extends AbstractCommand {

    private final TicketsConfiguration ticketsConfiguration;

    private final StorageService storageService;
    private final MessageService messageService;
    private final CommandArgumentFactory commandArgumentFactory;
    private final TicketTypeDescriptor ticketTypeDescriptor;
    private final LocationDescriptor locationDescriptor;

    @Inject
    public UserCommands(
        final TicketsConfiguration ticketsConfiguration,
        final CommandArgumentFactory commandArgumentFactory,
        final StorageService storageService,
        final MessageService messageService,
        final TicketTypeDescriptor ticketTypeDescriptor,
        final LocationDescriptor locationDescriptor
    ) {
        this.ticketsConfiguration = ticketsConfiguration;
        this.storageService = storageService;
        this.messageService = messageService;
        this.commandArgumentFactory = commandArgumentFactory;
        this.ticketTypeDescriptor = ticketTypeDescriptor;
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
                command.handler(ctx -> this.handleCreate(ctx, format))
            );
        }

        commandManager.command(root.literal("show")
            .handler(this::handleShow));
    }

    private Command.Builder<PlayerCommander> createSubCommand(
        final Command.Builder<PlayerCommander> base,
        final TicketFormat format
    ) {
        Command.Builder<PlayerCommander> command = base.literal(format.identifier());

        for (TicketFormatPart part : format.parts()) {
            ParserDescriptor<Commander, ?> parser = switch (part.style()) {
                case Player -> this.commandArgumentFactory.profile();
                case Sentence -> new LabeledDescriptor();
                case Location -> this.locationDescriptor;
            };

            command = command.required(part.identifier(), parser);
        }

        return command;
    }

    private void handleCreate(final CommandContext<PlayerCommander> context, TicketFormat format) {
        PlayerCommander commander = context.sender();
        TicketFormatContent content = new TicketFormatContent();

        for (TicketFormatPart part : format.parts()) {
            content.put(part.identifier(), context.get(part.identifier()));
        }

        Ticket ticket = this.storageService.createTicket(commander.uuid(), format, content);

        commander.sendMessage(
            text("Ticket created: " + ticket.id())
        );
    }

    private void handleShow(final CommandContext<PlayerCommander> context) {
        PlayerCommander commander = context.sender();

        Ticket ticket = this.storageService.findTickets(EnumSet.allOf(TicketStatus.class), commander.uuid(), null)
            .stream()
            .sorted(Comparator.comparing(Ticket::date))
            .findFirst()
            .orElseThrow();

        this.messageService.showTicket(context.sender().audience(), ticket);
    }
}
