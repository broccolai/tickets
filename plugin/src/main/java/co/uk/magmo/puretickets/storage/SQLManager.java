package co.uk.magmo.puretickets.storage;

import co.uk.magmo.puretickets.configuration.Config;
import co.uk.magmo.puretickets.storage.functions.HelpersSQL;
import co.uk.magmo.puretickets.storage.functions.MessageSQL;
import co.uk.magmo.puretickets.storage.functions.NotificationSQL;
import co.uk.magmo.puretickets.storage.functions.SettingsSQL;
import co.uk.magmo.puretickets.storage.functions.TicketFunctions;
import co.uk.magmo.puretickets.storage.platforms.MySQL;
import co.uk.magmo.puretickets.storage.platforms.Platform;
import co.uk.magmo.puretickets.storage.platforms.SQLite;
import org.bukkit.plugin.Plugin;

public class SQLManager {
    HelpersSQL helpers = new HelpersSQL();

    Platform platform;
    TicketFunctions ticket;
    MessageSQL message;
    NotificationSQL notification;
    SettingsSQL setting;

    public SQLManager(Plugin plugin, Config config) {
        if (config.STORAGE__MYSQL) {
            platform = new MySQL();
        } else {
            platform = new SQLite();
        }

        ticket = new TicketFunctions(helpers, platform);
        message = new MessageSQL(helpers);
        notification = new NotificationSQL(helpers);
        setting = new SettingsSQL(platform);

        helpers.setup(platform, message);
        platform.setup(plugin, config);
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
