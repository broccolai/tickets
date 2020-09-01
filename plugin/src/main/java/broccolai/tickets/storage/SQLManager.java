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

public class SQLManager {
    HelpersSQL helpers = new HelpersSQL();

    Platform platform;
    TicketSQL ticket;
    MessageSQL message;
    NotificationSQL notification;
    SettingsSQL setting;

    public SQLManager(Plugin plugin, Config config) {
        if (config.STORAGE__MYSQL) {
            platform = new MySQL();
        } else {
            platform = new SQLite();
        }

        ticket = new TicketSQL(helpers, platform);
        message = new MessageSQL(helpers);
        notification = new NotificationSQL(helpers);
        setting = new SettingsSQL(platform);

        helpers.setup(platform, message);
        platform.setup(plugin, config);
    }

    public TicketSQL getTicket() {
        return ticket;
    }

    public MessageSQL getMessage() {
        return message;
    }

    public NotificationSQL getNotification() {
        return notification;
    }

    public SettingsSQL getSetting() {
        return setting;
    }
}
