package love.broccolai.tickets.minecraft.common.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import java.util.EnumSet;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.minecraft.common.factory.CommandArgumentFactory;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.jspecify.annotations.NullMarked;

import static love.broccolai.tickets.api.model.TicketStatus.OPEN;
import static net.kyori.adventure.text.Component.text;

@NullMarked
public final class StaffCommands extends AbstractCommand {

    private final CommandArgumentFactory argumentFactory;
    private final StorageService storageService;
    private final ModificationService modificationService;

    @Inject
    public StaffCommands(
        final CommandArgumentFactory argumentFactory,
        final StorageService storageService,
        final ModificationService modificationService
    ) {
        this.argumentFactory = argumentFactory;
        this.storageService = storageService;
        this.modificationService = modificationService;
    }

    @Override
    public void register(final CommandManager<Commander> commandManager) {
        Command.Builder<Commander> root = commandManager
            .commandBuilder("tickets", "tis");

        commandManager.command(
            root.literal("list")
                .handler(this::handleList)
        );

        commandManager.command(
            root.literal("close")
                .required(this.argumentFactory.profileTicket("ticket", EnumSet.of(OPEN)))
                .handler(this::handleClose)
        );
    }

    public void handleList(CommandContext<Commander> context) {
        Commander commander = context.getSender();

        commander.sendMessage(text("all tickets"));

        Trove.of(this.storageService.findTickets(EnumSet.of(OPEN), null, null))
            .map(ticket -> text(ticket.id() + " with status + " + ticket.status().name()))
            .forEach(commander::sendMessage);
    }

    public void handleClose(CommandContext<Commander> context) {
        Commander commander = context.getSender();
        Ticket ticket = context.get("ticket");

        this.modificationService.close(ticket, commander.uuid());
    }
}
