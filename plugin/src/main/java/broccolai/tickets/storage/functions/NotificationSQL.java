package broccolai.tickets.storage.functions;

import co.aikar.idb.DB;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Notification SQL
 */
public final class NotificationSQL {

    private NotificationSQL() {
    }

    /**
     * Select all notifications from the Database and clear it
     *
     * @param uniqueId Users unique id
     * @return List of notifications
     */
    @NonNull
    public static List<String> retrieve(@NonNull final UUID uniqueId) {
        List<String> results;

        try {
            results = DB.getFirstColumnResults(
                    "SELECT message FROM puretickets_notification WHERE uuid = ?",
                    uniqueId.toString()
            );
            DB.executeUpdate("DELETE FROM puretickets_notification WHERE uuid = ?", uniqueId.toString());
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("Unable to load old notifications");
            return new ArrayList<>();
        }

        return results;
    }

    /**
     * Add a message to a players pending notification
     *
     * @param uniqueId Users unique id
     * @param message  String message to add
     */
    public static void add(@NonNull final UUID uniqueId, @NonNull final String message) {
        try {
            DB.executeInsert("INSERT INTO puretickets_notification(uuid, message) VALUES(?, ?)",
                    uniqueId.toString(), message
            );
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Failed to insert notification " + message);
        }
    }

}

