package co.uk.magmo.puretickets.storage;

import co.uk.magmo.puretickets.storage.FunctionInterfaces.*;
import org.bukkit.plugin.Plugin;

public abstract class SQLManager {
    HelpersSQL helpers = new HelpersSQL();

    Discrete discrete;
    TicketFunctions ticket = new TicketFunctions(helpers);
    MessageSQL message = new MessageSQL(helpers);
    NotificationSQL notification = new NotificationSQL(helpers);
    SettingsSQL setting = new SettingsSQL();

    public SQLManager(Discrete discrete) {
        this.discrete = discrete;
        helpers.setup(message);
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

    interface Discrete {
        void setup(Plugin plugin);
    }
}
