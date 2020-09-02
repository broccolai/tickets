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
        DiscordManager discordManager = new DiscordManager(this.getLogger(), config);
        SQLManager sqlManager = new SQLManager(this, config);

        UserManager userManager = new UserManager(sqlManager);
        TicketManager ticketManager = new TicketManager(config, pluginManager, sqlManager);
        CommandManager commandManager = new CommandManager(this, config, sqlManager.getTicket());

        taskManager = new TaskManager(this);
        notificationManager = new NotificationManager(config, sqlManager, taskManager, userManager, commandManager, discordManager);

        commandManager.registerInjections(config, userManager, ticketManager, notificationManager, taskManager, pluginManager, sqlManager.getTicket());
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
