package broccolai.tickets;

import broccolai.tickets.commands.CommandManager;
import broccolai.tickets.configuration.Config;
import broccolai.tickets.integrations.DiscordManager;
import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.storage.SQLManager;
import broccolai.tickets.tasks.TaskManager;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.user.UserManager;
import co.aikar.idb.DB;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class.
 */
public class PureTickets extends JavaPlugin {
    private TaskManager taskManager;
    private NotificationManager notificationManager;
    private PluginManager pluginManager;

    @Override
    public void onEnable() {
        pluginManager = getServer().getPluginManager();

        Config config = new Config(this);
        SQLManager.setup(this, config);

        DiscordManager discordManager = new DiscordManager(this.getLogger(), config);
        UserManager userManager = new UserManager();
        TicketManager ticketManager = new TicketManager(config, pluginManager);
        CommandManager commandManager = new CommandManager(this, config);

        taskManager = new TaskManager(this);
        notificationManager = new NotificationManager(config, taskManager, userManager, commandManager, discordManager);

        commandManager.registerInjections(config, userManager, ticketManager, notificationManager, taskManager, pluginManager);
        commandManager.registerCommands();

        registerEvents(notificationManager, ticketManager);
    }

    @Override
    public void onDisable() {
        if (taskManager != null) {
            taskManager.clear();
        }

        if (notificationManager != null) {
            notificationManager.save();
        }

        DB.close();
        HandlerList.unregisterAll(this);
    }

    /**
     * Register multiple events.
     */
    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }
}
