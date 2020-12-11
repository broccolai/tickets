package broccolai.tickets.bukkit.commands;

import broccolai.tickets.core.commands.TicketsCommandManager;
import broccolai.tickets.core.user.Soul;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

public final class BukkitTicketsCommandManager extends TicketsCommandManager<CommandSender> {

    private final Plugin plugin;

    /**
     * Initialise the Command Manager
     *
     * @param plugin Plugin instance
     */
    public BukkitTicketsCommandManager(final @NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected @NonNull CommandManager<Soul<CommandSender>> getCommandManager(
            @NonNull final Function<CommandSender, Soul<CommandSender>> commandSenderMapper,
            @NonNull final Function<Soul<CommandSender>, CommandSender> backwardsCommandSenderMapper
    ) {
        try {
            PaperCommandManager<Soul<CommandSender>> manager = new PaperCommandManager<>(
                    this.plugin,
                    AsynchronousCommandExecutionCoordinator.<Soul<CommandSender>>newBuilder().build(),
                    commandSenderMapper,
                    backwardsCommandSenderMapper
            );

            if (manager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                manager.registerAsynchronousCompletions();
            }

            return manager;
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PureTickets could not create a Command Manager");
        }
    }

}
