package love.broccolai.tickets.minecraft.common.command;

import com.google.inject.Inject;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketType;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.model.PlayerCommander;
import love.broccolai.tickets.minecraft.common.parsers.ticket.TicketTypeDescriptor;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.StringParser;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;

@NullMarked
public final class UserCommands extends AbstractCommand {

    private final StorageService storageService;
    private final ModificationService modificationService;
    private final TicketTypeDescriptor ticketTypeDescriptor;

    @Inject
    public UserCommands(
        final StorageService storageService,
        final ModificationService modificationService,
        final TicketTypeDescriptor ticketTypeDescriptor
    ) {
        this.storageService = storageService;
        this.modificationService = modificationService;
        this.ticketTypeDescriptor = ticketTypeDescriptor;
    }

    @Override
    public void register(final CommandManager<Commander> commandManager) {
        Command.Builder<PlayerCommander> root = commandManager
            .commandBuilder("ticket", "ti")
            .senderType(PlayerCommander.class);

        commandManager.command(
            root.literal("create")
                .required("type", this.ticketTypeDescriptor)
                .required("message", StringParser.greedyStringParser())
                .handler(this::handleCreate)
        );
    }

    private void handleCreate(final CommandContext<PlayerCommander> context) {
        PlayerCommander commander = context.sender();
        TicketType type = context.get("type");
        String message = context.get("message");

        Ticket ticket = this.storageService.createTicket(type, commander.uuid(), message);

        commander.sendMessage(
            text("Ticket created: " + ticket.id())
        );
    }
}
