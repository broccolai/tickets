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
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The Command Manager.
 */
public final class CommandManager extends PaperCommandManager<Soul> {

    /**
     * Initialise the Command Manager.
     *
     * @param plugin              Plugin instance
     * @param config              Configuration instance
     * @param userManager         User manager
     * @param notificationManager Notification manager
     * @param ticketManager       Ticket Manager
     * @param pluginManager       Plugin manager
     */
    public CommandManager(
            @NonNull final Plugin plugin,
            @NonNull final Config config,
            @NonNull final UserManager userManager,
            @NonNull final NotificationManager notificationManager,
            @NonNull final TicketManager ticketManager,
            @NonNull final PluginManager pluginManager
    ) throws Exception {
        super(
                plugin,
                AsynchronousCommandExecutionCoordinator.<Soul>newBuilder().build(),
                userManager::fromSender,
                Soul::asSender
        );

//        this.registerBrigadier();
        this.registerAsynchronousCompletions();
        this.registerCommandPreProcessor(context -> context.getCommandContext().store("ticketManager", ticketManager));

        new PureTicketsCommand(plugin, this);
        new TicketCommand(this, pluginManager, config, notificationManager, ticketManager);
        new TicketsCommand(this, config, userManager, notificationManager, ticketManager);
    }

}
