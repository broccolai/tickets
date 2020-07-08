package co.uk.magmo.puretickets.storage.functions;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import co.uk.magmo.puretickets.interactions.PendingNotification;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class NotificationSQL {
    private final HelpersSQL helpers;

    public NotificationSQL(HelpersSQL helpers) {
        this.helpers = helpers;
    }

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

