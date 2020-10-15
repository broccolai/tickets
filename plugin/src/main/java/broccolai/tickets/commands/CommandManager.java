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
public final class CommandManager extends PaperCommandManager<Soul> {

    /**
     * Initialise the Command Manager.
     *
     * @param plugin              Plugin instance
     * @param config              Configuration instance
     * @param ticketManager       Ticket manager
     * @param userManager         User manager
     * @param notificationManager Notification manager
     * @param pluginManager       Plugin manager
     */
    public CommandManager(
            @NotNull final Plugin plugin,
            @NotNull final Config config,
            @NotNull final TicketManager ticketManager,
            @NotNull final UserManager userManager,
            @NotNull final NotificationManager notificationManager,
            @NotNull final PluginManager pluginManager
    ) throws Exception {
        super(
                plugin,
                AsynchronousCommandExecutionCoordinator.<Soul>newBuilder().build(),
                userManager::fromSender,
                Soul::asSender
        );
        registerBrigadier();
        registerAsynchronousCompletions();

        new PureTicketsCommand(plugin, this);
        new TicketCommand(pluginManager, config, this, ticketManager, notificationManager);
        new TicketsCommand(this, config, userManager, ticketManager, notificationManager);
    }

}
