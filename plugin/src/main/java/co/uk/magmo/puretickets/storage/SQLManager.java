package co.uk.magmo.puretickets.storage;

import co.uk.magmo.puretickets.storage.functions.*;
import co.uk.magmo.puretickets.storage.platforms.Platform;

public class SQLManager {
    HelpersSQL helpers = new HelpersSQL();

    Platform platform;
    TicketFunctions ticket = new TicketFunctions(helpers);
    MessageSQL message = new MessageSQL(helpers);
    NotificationSQL notification = new NotificationSQL(helpers);
    SettingsSQL setting = new SettingsSQL();

    public SQLManager(Platform platform) {
        this.platform = platform;

        helpers.setup(platform, message);
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
