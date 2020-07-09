package co.uk.magmo.puretickets.storage.functions;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import co.uk.magmo.puretickets.storage.platforms.Platform;
import co.uk.magmo.puretickets.user.UserSettings;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.UUID;

public class SettingsSQL {
    private final Platform platform;

    public SettingsSQL(Platform platform) {
        this.platform = platform;
    }

    public UserSettings select(UUID uuid) {
        DbRow result;

        try {
            result = DB.getFirstRow("SELECT announcements from puretickets_settings WHERE uuid = ?",
                    uuid.toString());
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        boolean announcements = result.getString("announcements").equals("1");

        return new UserSettings(announcements);
    }

    public Boolean exists(UUID uuid) {
        try {
            return platform.getPureInteger(DB.getFirstColumn("SELECT EXISTS(SELECT 1 from puretickets_settings WHERE uuid = ?)",
                    uuid.toString())) == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    public void insert(UUID uuid, UserSettings settings) {
        try {
            DB.executeInsert("INSERT INTO puretickets_settings(uuid, announcements) VALUES(?, ?)",
                    uuid.toString(), settings.getAnnouncements());
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Failed to insert user settings " + uuid.toString());
        }
    }

    public void update(UUID uuid, UserSettings settings) {
        try {
            DB.executeUpdate("UPDATE puretickets_settings SET announcements = ? WHERE uuid = ?",
                    settings.getAnnouncements(), uuid.toString());
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Failed to update user settings " + uuid.toString());
        }
    }
}

