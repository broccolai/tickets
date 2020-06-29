package co.uk.magmo.puretickets.storage

import co.aikar.idb.*
import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.utils.asName
import org.bukkit.plugin.Plugin
import java.util.logging.Level
import kotlin.collections.HashMap

class MySQLManager : SQLManager {
    override fun setup(plugin: Plugin) {
        val options = DatabaseOptions.builder().mysql(Config.storageUser, Config.storagePassword, Config.storageName, Config.storageHost).build()
        val pooledOptions = PooledDatabaseOptions.builder().options(options).build()

        pooledOptions.dataSourceProperties = hashMapOf<String, Any>(Pair("useSSL", Config.storageSSL))

        val database = HikariPooledDatabase(pooledOptions)

        DB.setGlobalDatabase(database)
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS puretickets_ticket(id INTEGER, uuid TEXT, status TEXT, picker TEXT)")
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS puretickets_message(ticket INTEGER, reason TEXT, data TEXT, sender TEXT, date TEXT)")
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS puretickets_notification(uuid TEXT, message TEXT, replacements TEXT)")
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS puretickets_settings(uuid TEXT, announcements TEXT)")
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS puretickets_sql(version INTEGER)")

        var version = DB.getFirstColumn<Int>("SELECT version FROM puretickets_sql")

        if (version == null) {
            DB.executeInsert("INSERT INTO puretickets_sql(version) VALUES(?)", 0)
            version = 0
        }

        if (version == 0) {
            plugin.logger.log(Level.INFO, "Updated PureTickets database to have location column")
            DB.executeUpdate("ALTER TABLE puretickets_ticket ADD location TEXT")
            version++
        }

        if (version <= 1) {
            plugin.logger.log(Level.INFO, "Updated PureTickets database to remove tickets with empty locations and remove all pending notifications")
            DB.executeUpdate("DELETE from puretickets_ticket WHERE location IS NULL OR trim(location) = ?", "")
            DB.executeUpdate("DELETE from puretickets_notification")
            version++
        }

        DB.executeUpdate("UPDATE puretickets_sql SET version = ?", version)
    }

    override fun DbRow.getPureLong(column: String): Long {
        return getString(column).toLong()
    }
}