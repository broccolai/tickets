package broccolai.tickets.bukkit.commands;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.bukkit.model.BukkitPlayerSoul;
import broccolai.tickets.bukkit.model.BukkitTicketContextKeys;
import broccolai.tickets.core.commands.arguments.TicketParserMode;
import broccolai.tickets.core.commands.command.BaseCommand;
import broccolai.tickets.core.configuration.CommandsConfiguration;
import broccolai.tickets.core.configuration.MainConfiguration;
import broccolai.tickets.core.factory.CloudArgumentFactory;
import broccolai.tickets.core.utilities.Constants;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import io.papermc.lib.PaperLib;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.EnumSet;
import java.util.Optional;

public final class BukkitTicketsCommand implements BaseCommand {

    private final CommandsConfiguration.TicketsConfiguration config;
    private final CloudArgumentFactory argumentFactory;
    private final MessageService messageService;
    private final TaskService taskService;

    @Inject
    public BukkitTicketsCommand(
            final @NonNull MainConfiguration config,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull MessageService messageService,
            final @NonNull TaskService taskService
    ) {
        this.config = config.commandsConfiguration.tickets;
        this.argumentFactory = argumentFactory;
        this.messageService = messageService;
        this.taskService = taskService;
    }

    @Override
    public void register(
            final @NonNull CommandManager<OnlineSoul> manager
    ) {
        final Command.Builder<OnlineSoul> builder = manager.commandBuilder("tickets", "tis");

        manager.command(builder.literal(
                this.config.teleport.main,
                ArgumentDescription.of("Teleport to a tickets creation location"),
                this.config.teleport.aliases
                )
                        .senderType(PlayerSoul.class)
                        .permission(Constants.STAFF_PERMISSION + ".teleport")
                        .argument(this.argumentFactory.ticket("ticket", TicketParserMode.ANY, EnumSet.allOf(TicketStatus.class), 0))
                        .handler(this::processTeleport)
                        .build()
        );
    }

    private void processTeleport(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
        BukkitPlayerSoul soul = (BukkitPlayerSoul) c.getSender();
        Ticket ticket = c.get("ticket");
        Optional<Location> potentialLocation = ticket.context().get(BukkitTicketContextKeys.LOCATION);

        if (potentialLocation.isEmpty()) {
            //todo: Replace this
            System.out.println("LOCATION NOT STORED");
            return;
        }

        this.taskService.sync(() -> {
            Player player = soul.sender();
            PaperLib.teleportAsync(player, potentialLocation.get());
            Component component = this.messageService.commandsTeleport(ticket);
            soul.sendMessage(component);
        });
    }

}
