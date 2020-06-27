package co.uk.magmo.puretickets.storage;

import co.aikar.idb.DbRow;
import co.uk.magmo.puretickets.interactions.PendingNotification;
import co.uk.magmo.puretickets.locale.Messages;
import co.uk.magmo.puretickets.storage.FunctionInterfaces.*;
import co.uk.magmo.puretickets.ticket.Message;
import co.uk.magmo.puretickets.ticket.MessageReason;
import co.uk.magmo.puretickets.ticket.Ticket;
import co.uk.magmo.puretickets.ticket.TicketStatus;
import org.bukkit.Location;

import java.time.LocalDateTime;
import java.util.*;

public abstract class SQLManager {
    MiscellaneousFunctions miscellaneous;
    TicketFunctions ticket;
    MessageFunctions message;
    NotificationFunctions notification;
    SettingsFunctions setting;
    HelperFunctions helpers;

    public SQLManager(MiscellaneousFunctions miscellaneous, TicketFunctions ticket, MessageFunctions message, NotificationFunctions notification, SettingsFunctions setting, HelperFunctions helpers) {
        this.miscellaneous = miscellaneous;
        this.ticket = ticket;
        this.message = message;
        this.notification = notification;
        this.setting = setting;
        this.helpers = helpers;
    }

    public MiscellaneousFunctions getMiscellaneous() {
        return miscellaneous;
    }

    public TicketFunctions getTicket() {
        return ticket;
    }

    public MessageFunctions getMessage() {
        return message;
    }

    public NotificationFunctions getNotification() {
        return notification;
    }

    public SettingsFunctions getSetting() {
        return setting;
    }

    public HelperFunctions getHelpers() {
        return helpers;
    }

    // Helper Functions

    protected Ticket buildTicket(DbRow row) {
        Integer id = row.getInt("id");
        UUID player = helpers.getUUID(row, "uuid");
        ArrayList<Message> messages = null; // = row.getInt("id");
        TicketStatus status = helpers.getEnumValue(row, TicketStatus.class, "status");
        Location location = helpers.getLocation(row, "location");
        UUID picker = helpers.getUUID(row, "picker");

        return new Ticket(id, player, messages, status, location, picker);
    }

    protected Message buildMessage(DbRow row) {
        MessageReason reason = helpers.getEnumValue(row, MessageReason.class, "reason");
        LocalDateTime date = helpers.getDate(row, "date");

        String data = row.getString("data");

        if (data != null) {
            return new Message(reason, date, data);
        }

        UUID sender = helpers.getUUID(row, "sender");

        if (sender != null) {
            return new Message(reason, date, sender);
        }

        throw new IllegalArgumentException();
    }

    protected PendingNotification buildNotification(DbRow row) {
        Messages message = helpers.getEnumValue(row, Messages.class, "message");
        String[] replacements = row.getString("replacements").split("\\|");

        return new PendingNotification(message, replacements);
    }
}
