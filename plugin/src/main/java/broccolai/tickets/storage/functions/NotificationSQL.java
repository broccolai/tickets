package broccolai.tickets.storage.functions;

import co.aikar.idb.DB;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * Notification SQL.
 */
public class NotificationSQL {
    /**
     * Select all notifications from the Database and clear it.
     *
     * @return a list of notifications
     */
    @NotNull
    public static List<String> retrieve(@NotNull final UUID uniqueId) {
        List<String> results;

        try {
            results = DB.getFirstColumnResults("SELECT message FROM puretickets_notification WHERE uuid = ?", uniqueId.toString());
            DB.executeUpdate("DELETE FROM puretickets_notification WHERE uuid = ?", uniqueId.toString());
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("Unable to load old notifications");
            return new ArrayList<>();
        }

        return results;
    }

    /**
     * Add a message to a players pending notification.
     *
     * @param uniqueId Users unique id
     * @param message  String message to add
     */
    public static void add(@NotNull final UUID uniqueId, @NotNull final String message) {
        try {
            DB.executeInsert("INSERT INTO puretickets_notification(uuid, message) VALUES(?, ?)",
                uniqueId.toString(), message);
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Failed to insert notification " + message);
        }
    }

}

