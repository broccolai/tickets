package broccolai.tickets.storage;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.storage.functions.HelpersSQL;
import broccolai.tickets.storage.functions.SettingsSQL;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.storage.platforms.MySQL;
import broccolai.tickets.storage.platforms.Platform;
import broccolai.tickets.storage.platforms.SQLite;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * SQL Manager
 */
public final class SQLManager {

    private SQLManager() {
    }

    /**
     * Setup the SQL Manager.
     *
     * @param plugin the plugin instance
     * @param config the configuration instance
     */
    public static void setup(@NotNull final Plugin plugin, @NotNull final Config config) {
        Platform platform = config.getStorageMySQL() ? new MySQL() : new SQLite();
        HelpersSQL.setup(platform);
        SettingsSQL.setup(platform);
        TicketSQL.setup(platform);
        platform.setup(plugin, config);
    }

}
