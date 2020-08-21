package broccolai.tickets.storage.functions;

import broccolai.tickets.ticket.Message;
import broccolai.tickets.ticket.Ticket;
import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;

public class MessageSQL {
    HelpersSQL helpers;

    public MessageSQL(HelpersSQL helpers) {
        this.helpers = helpers;
    }

    public List<Message> selectAll(Integer id) {
        List<DbRow> results;

        try {
            results = DB.getResults("SELECT reason, data, sender, date from puretickets_message WHERE ticket = ?", id);
        } catch (SQLException e) {
            return new ArrayList<>();
        }

        List<Message> output = new ArrayList<>();

        for (DbRow result : results) {
            output.add(helpers.buildMessage(result));
        }

        return output;
    }

    public void insert(Ticket ticket, Message message) {
        UUID sender = message.getSender();
        Long date = helpers.serializeLocalDateTime(message.getDate());
        String senderName;

        if (sender == null) {
            senderName = null;
        } else {
            senderName = sender.toString();
        }

        try {
            DB.executeInsert("INSERT INTO puretickets_message(ticket, reason, data, sender, date) VALUES(?, ?, ?, ?, ?)",
                ticket.getId(), message.getReason().name(), message.getData(), senderName, date);
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Failed to insert message, ticket id #" + ticket.getId());
        }
    }
}

