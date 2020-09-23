package broccolai.tickets.storage;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.storage.functions.HelpersSQL;
import broccolai.tickets.storage.functions.MessageSQL;
import broccolai.tickets.storage.functions.NotificationSQL;
import broccolai.tickets.storage.functions.SettingsSQL;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.storage.platforms.MySQL;
import broccolai.tickets.storage.platforms.Platform;
import broccolai.tickets.storage.platforms.SQLite;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * SQL Manager.
 */
public class SQLManager {
    /**
     * Setup the SQL Manager.
     *
     * @param plugin the plugin instance
     * @param config the configuration instance
     */
    public static void setup(Plugin plugin, Config config) {
        Platform platform = config.STORAGE__MYSQL ? new MySQL() : new SQLite();
        HelpersSQL.setup(platform);
        SettingsSQL.setup(platform);
        TicketSQL.setup(platform);
        platform.setup(plugin, config);
    }
}
