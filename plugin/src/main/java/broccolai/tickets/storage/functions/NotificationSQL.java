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
import org.jetbrains.annotations.NotNull;

/**
 * Notification SQL.
 */
public class NotificationSQL {
    @NotNull
    private final HelpersSQL helpers;

    /**
     * Initialise Notification SQL.
     *
     * @param helpers the helper SQL
     */
    public NotificationSQL(@NotNull HelpersSQL helpers) {
        this.helpers = helpers;
    }

    /**
     * Select all notifications from the Database and clear it.
     *
     * @return a multimap with uuids to notifications
     */
    public Multimap<UUID, PendingNotification> selectAllAndClear() {
        Multimap<UUID, PendingNotification> output = ArrayListMultimap.create();
        List<DbRow> results;

        try {
            results = DB.getResults("SELECT uuid, message, replacements from puretickets_notification");
            DB.executeUpdate("DELETE from puretickets_notification");
        } catch (SQLException e) {
            return output;
        }

        for (DbRow result : results) {
            UUID uuid = helpers.getUUID(result, "uuid");
            PendingNotification notification = helpers.buildNotification(result);

            output.put(uuid, notification);
        }

        return output;
    }

    /**
     * Insert all pending notifications back into the Database.
     *
     * @param notifications the multimap to save
     */
    public void insertAll(Multimap<UUID, PendingNotification> notifications) {
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

