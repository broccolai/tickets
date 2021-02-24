package broccolai.tickets.bukkit.commands;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.bukkit.model.User.BukkitOnlineSoul;
import broccolai.tickets.core.commands.TicketsCommandManager;
import broccolai.tickets.core.service.UserService;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BukkitTicketsCommandManager extends TicketsCommandManager<CommandSender> {

    private final Plugin plugin;
    private final UserService<CommandSender, Player> userService;

    /**
     * Initialise the Command Manager
     *
     * @param plugin Plugin instance
     */
    public BukkitTicketsCommandManager(
            final @NonNull Plugin plugin,
            final @NonNull UserService<CommandSender, Player> userService
    ) {
        this.plugin = plugin;
        this.userService = userService;
    }

    @Override
    protected @NonNull CommandManager<@NonNull OnlineSoul> getCommandManager() {
        try {
            PaperCommandManager<OnlineSoul> manager = new PaperCommandManager<>(
                    this.plugin,
                    AsynchronousCommandExecutionCoordinator.<OnlineSoul>newBuilder().build(),
                    this.userService::wrap,
                    sender -> sender.<BukkitOnlineSoul>cast().sender()
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
