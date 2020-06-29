package co.uk.magmo.puretickets.storage;

import co.uk.magmo.puretickets.storage.FunctionInterfaces.*;
import org.bukkit.plugin.Plugin;

public class SQLManager {
    HelpersSQL helpers = new HelpersSQL();

    PlatformFunctions platformFunctions;
    TicketFunctions ticket = new TicketFunctions(helpers);
    MessageSQL message = new MessageSQL(helpers);
    NotificationSQL notification = new NotificationSQL(helpers);
    SettingsSQL setting = new SettingsSQL();

    public SQLManager(PlatformFunctions platformFunctions) {
        this.platformFunctions = platformFunctions;

        helpers.setup(platformFunctions, message);
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
