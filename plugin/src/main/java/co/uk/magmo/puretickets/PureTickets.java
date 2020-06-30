package co.uk.magmo.puretickets;

import co.aikar.idb.DB;
import co.uk.magmo.puretickets.commands.CommandManager;
import co.uk.magmo.puretickets.configuration.Config;
import co.uk.magmo.puretickets.integrations.DiscordManager;
import co.uk.magmo.puretickets.interactions.NotificationManager;
import co.uk.magmo.puretickets.storage.SQLManager;
import co.uk.magmo.puretickets.storage.platforms.MySQL;
import co.uk.magmo.puretickets.storage.platforms.SQLite;
import co.uk.magmo.puretickets.tasks.TaskManager;
import co.uk.magmo.puretickets.ticket.TicketManager;
import co.uk.magmo.puretickets.user.UserManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class PureTickets extends JavaPlugin {
    private TaskManager taskManager;
    private NotificationManager notificationManager;

    @Override
    public void onEnable() {
        Config config = new Config(this);
        SQLManager sqlManager;

        if (config.STORAGE_MYSQL) {
            sqlManager = new SQLManager(new MySQL());
        } else {
            sqlManager = new SQLManager(new SQLite());
        }

        sqlManager.getPlatform().setup(this, config);

        UserManager userManager = new UserManager(sqlManager);
        DiscordManager discordManager = new DiscordManager(config);
        CommandManager commandManager = new CommandManager(this, config);
        TicketManager ticketManager = new TicketManager(sqlManager);

        taskManager = new TaskManager(this);
        notificationManager = new NotificationManager(config, sqlManager, taskManager, userManager, commandManager, discordManager, ticketManager);

        commandManager.registerCompletions(ticketManager);
        commandManager.registerInjections(userManager, ticketManager, notificationManager, taskManager);
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
