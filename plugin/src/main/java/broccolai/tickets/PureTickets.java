package broccolai.tickets;

import broccolai.tickets.commands.CommandManager;
import broccolai.tickets.configuration.Config;
import broccolai.tickets.integrations.DiscordManager;
import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.locale.LocaleManager;
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
    private PluginManager pluginManager;

    @Override
    public void onEnable() {
        pluginManager = getServer().getPluginManager();

        Config config = new Config(this);
        LocaleManager localeManager = LocaleManager.create(this);
        SQLManager.setup(this, config);

        DiscordManager discordManager = new DiscordManager(this.getLogger(), config);
        UserManager userManager = new UserManager(pluginManager, taskManager);
        TicketManager ticketManager = new TicketManager(config, pluginManager);
        taskManager = new TaskManager(this);
        NotificationManager notificationManager = new NotificationManager(config, taskManager, localeManager, userManager, discordManager);

        try {
            new CommandManager(this, config, ticketManager, userManager, notificationManager, pluginManager);
        } catch (Exception e) {
            return;
        }

        registerEvents(notificationManager, ticketManager);
    }

    @Override
    public void onDisable() {
        if (taskManager != null) {
            taskManager.clear();
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
