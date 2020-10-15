package broccolai.tickets.storage.functions;

import broccolai.tickets.storage.platforms.Platform;
import broccolai.tickets.user.UserSettings;
import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Setting SQL.
 */
public final class SettingsSQL {

    private SettingsSQL() {
    }

    private static Platform platform;

    /**
     * Initialise Settings SQL.
     *
     * @param platformInstance the platform to use
     */
    public static void setup(@NonNull final Platform platformInstance) {
        platform = platformInstance;
    }

    /**
     * Select UserSettings using a players unique id.
     *
     * @param uuid the players unique id
     * @return the constructed UserSettings
     */
    @NonNull
    public static UserSettings select(@NonNull final UUID uuid) {
        DbRow result;

        try {
            result = DB.getFirstRow(
                    "SELECT announcements from puretickets_settings WHERE uuid = ?",
                    uuid.toString()
            );
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        boolean announcements = result.getString("announcements").equals("1");

        return new UserSettings(announcements);
    }

    /**
     * Checks if a has user settings already.
     *
     * @param uuid the players unique id
     * @return a boolean
     */
    public static boolean exists(@NonNull final UUID uuid) {
        try {
            return platform.getPureInteger(DB.getFirstColumn(
                    "SELECT EXISTS(SELECT 1 from puretickets_settings WHERE uuid = ?)",
                    uuid.toString()
            )) == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Inserts a user setting instance into the database.
     *
     * @param uuid     the players unique id
     * @param settings the users settings
     */
    public static void insert(@NonNull final UUID uuid, @NonNull final UserSettings settings) {
        try {
            DB.executeInsert("INSERT INTO puretickets_settings(uuid, announcements) VALUES(?, ?)",
                    uuid.toString(), settings.getAnnouncements()
            );
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Failed to insert user settings " + uuid.toString());
        }
    }

    /**
     * Updates a users settings to a new instance.
     *
     * @param uuid     the players unique id
     * @param settings the users settings
     */
    public static void update(@NonNull final UUID uuid, @NonNull final UserSettings settings) {
        try {
            DB.executeUpdate("UPDATE puretickets_settings SET announcements = ? WHERE uuid = ?",
                    settings.getAnnouncements(), uuid.toString()
            );
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Failed to update user settings " + uuid.toString());
        }
    }

}

