package broccolai.tickets;

import broccolai.tickets.commands.CommandManager;
import broccolai.tickets.configuration.Config;
import broccolai.tickets.integrations.DiscordManager;
import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.locale.LocaleManager;
import broccolai.tickets.storage.SQLPlatforms;
import broccolai.tickets.tasks.TaskManager;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.user.UserManager;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

@SuppressWarnings("unused")
public final class PureTickets extends JavaPlugin {

    private TaskManager taskManager;
    private UserManager userManager;
    private NotificationManager notificationManager;
    private PluginManager pluginManager;

    @Override
    public void onEnable() {
        pluginManager = getServer().getPluginManager();

        Config config = new Config(this);
        Jdbi jdbi = SQLPlatforms.construct(this, config);
        LocaleManager localeManager = LocaleManager.create(this);

        taskManager = new TaskManager(this);
        userManager = new UserManager(pluginManager, taskManager, localeManager, jdbi);
        DiscordManager discordManager = new DiscordManager(this.getLogger(), config);
        TicketManager ticketManager = new TicketManager(config, pluginManager, jdbi, taskManager);

        notificationManager = new NotificationManager(
                jdbi,
                config,
                taskManager,
                localeManager,
                userManager,
                discordManager,
                ticketManager
        );

        try {
            new CommandManager(this, config, userManager, notificationManager, ticketManager, pluginManager);
        } catch (Exception e) {
            return;
        }

        registerEvents(userManager, notificationManager, ticketManager, ticketManager.getIdStorage());
    }

    @Override
    public void onDisable() {
        if (this.taskManager != null) {
            this.taskManager.clear();
        }

        if (this.notificationManager != null) {
            this.notificationManager.saveAll();
        }

        if (this.userManager != null) {
            this.userManager.saveAll();
        }

        HandlerList.unregisterAll(this);
    }

    /**
     * Register multiple events
     *
     * @param listeners Listeners to register
     */
    private void registerEvents(final @NonNull Listener... listeners) {
        for (final Listener listener : listeners) {
            this.pluginManager.registerEvents(listener, this);
        }
    }

}
