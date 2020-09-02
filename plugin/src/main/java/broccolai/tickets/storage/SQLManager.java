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
    @NotNull
    private final TicketSQL ticket;
    @NotNull
    private final MessageSQL message;
    @NotNull
    private final NotificationSQL notification;
    @NotNull
    private final SettingsSQL setting;

    /**
     * Initialise the SQL Manager.
     *
     * @param plugin the plugin instance
     * @param config the configuration instance
     */
    public SQLManager(Plugin plugin, Config config) {
        Platform platform = config.STORAGE__MYSQL ? new MySQL() : new SQLite();
        HelpersSQL helpers = new HelpersSQL();

        ticket = new TicketSQL(helpers, platform);
        message = new MessageSQL(helpers);
        notification = new NotificationSQL(helpers);
        setting = new SettingsSQL(platform);

        helpers.setup(platform, message);
        platform.setup(plugin, config);
    }

    /**
     * Retrieve the ticket sql.
     *
     * @return TicketSQL
     */
    @NotNull
    public TicketSQL getTicket() {
        return ticket;
    }

    /**
     * Retrieve the message sql.
     *
     * @return MessageSQL
     */
    @NotNull
    public MessageSQL getMessage() {
        return message;
    }

    /**
     * Retrieve the notification sql.
     *
     * @return NotificationSQL
     */
    @NotNull
    public NotificationSQL getNotification() {
        return notification;
    }

    /**
     * Retrieve the settings sql.
     *
     * @return SettingsSQL
     */
    @NotNull
    public SettingsSQL getSetting() {
        return setting;
    }
}
