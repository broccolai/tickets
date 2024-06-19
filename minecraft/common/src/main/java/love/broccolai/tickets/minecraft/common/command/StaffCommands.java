package love.broccolai.tickets.minecraft.common.command;

import com.google.inject.Inject;
import java.util.EnumSet;
import java.util.UUID;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.minecraft.common.factory.CommandArgumentFactory;
import love.broccolai.tickets.minecraft.common.model.Commander;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.jspecify.annotations.NullMarked;

import static love.broccolai.tickets.api.model.TicketStatus.OPEN;
import static love.broccolai.tickets.api.model.TicketStatus.PICKED;
import static net.kyori.adventure.text.Component.text;

@NullMarked
public final class StaffCommands extends AbstractCommand {

    private final static CloudKey<Ticket> TICKET_KEY = CloudKey.cloudKey("ticket", Ticket.class);
    private final static CloudKey<Profile> TARGET_KEY = CloudKey.cloudKey("profile", Profile.class);

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
            root.literal("show")
                .handler(this::handleShow)
        );

        commandManager.command(
            root.literal("list")
                .handler(this::handleList)
        );

        commandManager.command(
            root.literal("assign")
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.of(OPEN, PICKED)))
                .optional(TARGET_KEY, this.argumentFactory.profile())
                .handler(this::handleAssign)
        );

        commandManager.command(
            root.literal("close")
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.of(OPEN, PICKED)))
                .handler(this::handleClose)
        );
    }

    public void handleShow(final CommandContext<Commander> context) {
        Component.join(
            JoinConfiguration.newlines(),
            Component.text("")
        );
    }

    public void handleList(final CommandContext<Commander> context) {
        Commander commander = context.sender();

        commander.sendMessage(text("all tickets"));

        Trove.of(this.storageService.findTickets(EnumSet.of(OPEN), null, null))
            .map(ticket -> text(ticket.id() + " with status + " + ticket.status().name()))
            .forEach(commander::sendMessage);
    }

    public void handleAssign(final CommandContext<Commander> context) {
        Commander commander = context.sender();
        UUID target = context.optional(TARGET_KEY)
            .map(Profile::uuid)
            .orElse(commander.uuid());

        this.modificationService.assign(
            context.get(TICKET_KEY),
            commander.uuid(),
            target
        );
    }

    public void handleClose(final CommandContext<Commander> context) {
        Commander commander = context.sender();
        Ticket ticket = context.get("ticket");

        this.modificationService.close(ticket, commander.uuid());
    }
}
