package broccolai.tickets.storage.platforms;

import broccolai.tickets.configuration.Config;
import co.aikar.idb.DB;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.DbRow;
import co.aikar.idb.HikariPooledDatabase;
import co.aikar.idb.PooledDatabaseOptions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public final class SQLite implements Platform {

    @Override
    public void setup(@NonNull final Plugin plugin, @NonNull final Config config) {
        int version = 0;
        File file = new File(plugin.getDataFolder(), "tickets.db");

        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        DatabaseOptions options = DatabaseOptions.builder().sqlite(file.toString()).build();
        HikariPooledDatabase database = PooledDatabaseOptions.builder().options(options).createHikariDatabase();

        DB.setGlobalDatabase(database);

        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS ticket(id INTEGER, uuid TEXT, status TEXT, picker TEXT)");
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS message(ticket INTEGER, reason TEXT, data TEXT, sender TEXT, date TEXT)");
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS notification(uuid TEXT, message TEXT, replacements TEXT)");
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS settings(uuid TEXT, announcements TEXT)");

            version = DB.getFirstColumn("PRAGMA user_version");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        try {
            if (version == 0) {
                plugin.getLogger().info("Updated PureTickets database to have location column");
                DB.executeUpdate("ALTER TABLE ticket ADD location TEXT");
                version++;
            }
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning("Could not update location column");
        }

        try {
            if (version <= 1) {
                plugin.getLogger().info(
                        "Updated PureTickets database to remove tickets with empty locations and remove all pending notifications");
                DB.executeUpdate("DELETE FROM ticket WHERE location IS NULL OR trim(location) = ?", "");
                DB.executeUpdate("DELETE FROM notification");
                version++;
            }
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning("Could not remove location column");
        }

        try {
            if (version <= 2) {
                plugin.getLogger().info("Updated PureTickets database to prefix table names");
                DB.getFirstColumn("ALTER TABLE ticket RENAME TO puretickets_ticket;");
                DB.getFirstColumn("ALTER TABLE message RENAME TO puretickets_message;");
                DB.getFirstColumn("ALTER TABLE notification RENAME TO puretickets_notification;");
                DB.getFirstColumn("ALTER TABLE settings RENAME TO puretickets_settings;");
                version++;
            }
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning("Could not prefix column");
        }

        try {
            DB.executeUpdate("PRAGMA user_version = " + version);
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    @Override
    public Long getPureLong(@NonNull final DbRow row, @NonNull final String column) {
        return Long.valueOf(row.getString(column));
    }

    @Override
    public Integer getPureInteger(@NonNull final Object value) {
        return (Integer) value;
    }

}
