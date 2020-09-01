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
import org.jetbrains.annotations.NotNull;

/**
 * Message SQL.
 */
public class MessageSQL {
    @NotNull
    HelpersSQL helpers;

    /**
     * Initialise MessageSQL.
     *
     * @param helpers the helper sql instance
     */
    public MessageSQL(@NotNull HelpersSQL helpers) {
        this.helpers = helpers;
    }

    /**
     * Get all messages with an id.
     *
     * @param id the tickets id
     * @return a list of messages
     */
    public List<Message> selectAll(int id) {
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

    /**
     * Insert a message.
     *
     * @param ticket  the ticket instance to use
     * @param message the message to insert
     */
    public void insert(Ticket ticket, Message message) {
        UUID sender = message.getSender();
        long date = helpers.serializeLocalDateTime(message.getDate());
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

