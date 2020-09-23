package broccolai.tickets.storage.functions;

import broccolai.tickets.interactions.PendingNotification;
import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;

/**
 * Notification SQL.
 */
public class NotificationSQL {
    /**
     * Select all notifications from the Database and clear it.
     *
     * @return a multimap with uuids to notifications
     */
    public static Multimap<UUID, PendingNotification> selectAllAndClear() {
        Multimap<UUID, PendingNotification> output = ArrayListMultimap.create();
        List<DbRow> results;

        try {
            results = DB.getResults("SELECT uuid, message, replacements from puretickets_notification");
            DB.executeUpdate("DELETE from puretickets_notification");
        } catch (SQLException e) {
            return output;
        }

        for (DbRow result : results) {
            UUID uuid = HelpersSQL.getUUID(result, "uuid");
            PendingNotification notification = HelpersSQL.buildNotification(result);

            output.put(uuid, notification);
        }

        return output;
    }

    /**
     * Insert all pending notifications back into the Database.
     *
     * @param notifications the multimap to save
     */
    public static void insertAll(Multimap<UUID, PendingNotification> notifications) {
        notifications.forEach(((uuid, notification) -> {
            String message = notification.getMessageKey().name();
            String replacements = String.join("|", notification.getReplacements());

            try {
                DB.executeInsert("INSERT INTO puretickets_notification(uuid, message, replacements) VALUES(?, ?, ?)",
                    uuid.toString(), message, replacements);
            } catch (SQLException e) {
                Bukkit.getLogger().warning("Failed to insert notification " + message);
            }
        }));
    }
}

