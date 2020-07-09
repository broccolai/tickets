package co.uk.magmo.puretickets.storage.platforms;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.DbRow;
import co.aikar.idb.HikariPooledDatabase;
import co.aikar.idb.PooledDatabaseOptions;
import co.uk.magmo.puretickets.configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

public class MySQL implements Platform {
    @Override
    public void setup(Plugin plugin, Config config) {
        Integer version = null;
        DatabaseOptions options = DatabaseOptions.builder().mysql(config.STORAGE__USER, config.STORAGE__PASSWORD, config.STORAGE__NAME, config.STORAGE__HOST).build();
        PooledDatabaseOptions pooledOptions = PooledDatabaseOptions.builder().options(options).build();

        HashMap<String, Object> properties = new HashMap<>();

        properties.put("useSSL", config.STORAGE__SSL);

        pooledOptions.setDataSourceProperties(properties);

        Database database = new HikariPooledDatabase(pooledOptions);

        DB.setGlobalDatabase(database);

        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS puretickets_ticket(id INTEGER, uuid TEXT, status TEXT, picker TEXT)");
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS puretickets_message(ticket INTEGER, reason TEXT, data TEXT, sender TEXT, date TEXT)");
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS puretickets_notification(uuid TEXT, message TEXT, replacements TEXT)");
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS puretickets_settings(uuid TEXT, announcements TEXT)");
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS puretickets_sql(version INTEGER)");

            version = DB.getFirstColumn("SELECT version FROM puretickets_sql");
        } catch (SQLException e) {
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        try {
            if (version == null) {
                DB.executeInsert("INSERT INTO puretickets_sql(version) VALUES(?)", 0);
                version = 0;
            }

            if (version == 0) {
                plugin.getLogger().log(Level.INFO, "Updated PureTickets database to have location column");
                DB.executeUpdate("ALTER TABLE puretickets_ticket ADD location TEXT");
                version++;
            }

            if (version <= 1) {
                plugin.getLogger().log(Level.INFO, "Updated PureTickets database to remove tickets with empty locations and remove all pending notifications");
                DB.executeUpdate("DELETE from puretickets_ticket WHERE location IS NULL OR trim(location) = ?", "");
                DB.executeUpdate("DELETE from puretickets_notification");
                version++;
            }
        } catch (SQLException e) {
            Bukkit.getPluginManager().disablePlugin(plugin);
        } finally {
            try {
                DB.executeUpdate("UPDATE puretickets_sql SET version = ?", version);
            } catch (SQLException e) {
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
    }

    @Override
    public Long getPureLong(DbRow row, String column) {
        return Long.valueOf(row.getString(column));
    }

    @Override
    public Integer getPureInteger(Object value) {
        return ((Long) value).intValue();
    }
}