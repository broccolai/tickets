package broccolai.tickets.commands;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.user.Soul;
import broccolai.tickets.user.UserManager;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

/**
 * The Command Manager.
 */
public class CommandManager extends PaperCommandManager<Soul> {
    @NotNull
    final PluginManager pluginManager;
    @NotNull
    final TicketManager ticketManager;
    @NotNull
    final UserManager userManager;
    @NotNull
    final NotificationManager notificationManager;

    /**
     * Initialise the Command Manager.
     *
     * @param plugin the plugin to register commands against
     * @param config the config instance to use
     */
    public CommandManager(@NotNull Plugin plugin, @NotNull Config config, @NotNull TicketManager ticketManager, @NotNull UserManager userManager,
                          @NotNull NotificationManager notificationManager, @NotNull PluginManager pluginManager) throws Exception {
        super(plugin, AsynchronousCommandExecutionCoordinator.<Soul>newBuilder().build(), userManager::fromSender, Soul::asSender);
        this.pluginManager = pluginManager;
        this.ticketManager = ticketManager;
        this.userManager = userManager;
        this.notificationManager = notificationManager;
        registerBrigadier();
        registerAsynchronousCompletions();

        new PureTicketsCommand(plugin, this);
        new TicketCommand(pluginManager, config, this, ticketManager, notificationManager);
        new TicketsCommand(this, config, userManager, ticketManager, notificationManager);
    }
}
