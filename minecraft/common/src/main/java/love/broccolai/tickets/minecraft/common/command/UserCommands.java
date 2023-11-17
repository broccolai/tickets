package love.broccolai.tickets.minecraft.common.command;

import cloud.commandframework.Command.Builder;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.model.PlayerCommander;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;

@NullMarked
public final class UserCommands extends AbstractCommand {

    private final StorageService storageService;
    private final ModificationService modificationService;

    @Inject
    public UserCommands(
        final StorageService storageService,
        final ModificationService modificationService
    ) {
        this.storageService = storageService;
        this.modificationService = modificationService;
    }

    @Override
    public void register(final CommandManager<Commander> commandManager) {
        Builder<PlayerCommander> root = commandManager
            .commandBuilder("ticket", "ti")
            .senderType(PlayerCommander.class);

        commandManager.command(
            root.literal("create")
                .required(StringArgument.greedy("message"))
                .handler(this::handleCreate)
        );
    }

    private void handleCreate(final CommandContext<PlayerCommander> context) {
        PlayerCommander commander = context.getSender();
        String message = context.get("message");

        Ticket ticket = this.storageService.createTicket(commander.uuid(), message);

        commander.sendMessage(
            text("Ticket created: " + ticket.id())
        );
    }
}
