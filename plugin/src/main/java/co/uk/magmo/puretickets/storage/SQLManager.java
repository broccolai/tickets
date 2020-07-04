package co.uk.magmo.puretickets.storage;

import co.uk.magmo.puretickets.configuration.Config;
import co.uk.magmo.puretickets.storage.functions.*;
import co.uk.magmo.puretickets.storage.platforms.MySQL;
import co.uk.magmo.puretickets.storage.platforms.Platform;
import co.uk.magmo.puretickets.storage.platforms.SQLite;
import org.bukkit.plugin.Plugin;

public class SQLManager {
    HelpersSQL helpers = new HelpersSQL();

    Platform platform;
    TicketFunctions ticket = new TicketFunctions(helpers);
    MessageSQL message = new MessageSQL(helpers);
    NotificationSQL notification = new NotificationSQL(helpers);
    SettingsSQL setting = new SettingsSQL();

    public SQLManager(Plugin plugin, Config config) {
        if (config.STORAGE__MYSQL) {
            platform = new MySQL();
        } else {
            platform = new SQLite();
        }

        helpers.setup(platform, message);
        platform.setup(plugin, config);
    }

    public Platform getPlatform() {
        return platform;
    }

    public TicketFunctions getTicket() {
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
