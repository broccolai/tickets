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
import org.bukkit.plugin.java.JavaPlugin;

public class PureTickets extends JavaPlugin {
    private TaskManager taskManager;
    private NotificationManager notificationManager;

    @Override
    public void onEnable() {
        Config config = new Config(this);
        DiscordManager discordManager = new DiscordManager(this.getLogger(), config);
        SQLManager sqlManager = new SQLManager(this, config);

        UserManager userManager = new UserManager(sqlManager);
        TicketManager ticketManager = new TicketManager(config, sqlManager);
        CommandManager commandManager = new CommandManager(this, config, ticketManager);

        taskManager = new TaskManager(this);
        notificationManager = new NotificationManager(config, sqlManager, taskManager, userManager, commandManager, discordManager, ticketManager);

        commandManager.registerCompletions(ticketManager);
        commandManager.registerInjections(config, userManager, ticketManager, notificationManager, taskManager);
        commandManager.registerCommands();

        getServer().getPluginManager().registerEvents(notificationManager, this);
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
}
