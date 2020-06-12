package co.uk.magmo.puretickets.storage

import co.aikar.idb.*
import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.interactions.PendingNotification
import co.uk.magmo.puretickets.ticket.Message
import co.uk.magmo.puretickets.ticket.Ticket
import co.uk.magmo.puretickets.ticket.TicketStatus
import co.uk.magmo.puretickets.user.UserSettings
import co.uk.magmo.puretickets.utils.asName
import com.google.common.collect.ArrayListMultimap
import org.bukkit.plugin.Plugin
import org.intellij.lang.annotations.Language
import java.util.*
import java.util.logging.Level

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

    override val ticket = object : SQLManager.TicketFunctions {
        override fun currentId(): Int {
            return DB.getFirstColumn<Int>("SELECT max(id) from puretickets_ticket") ?: 0
        }

        override fun selectActive(): ArrayListMultimap<UUID, Ticket> {
            val multimap = ArrayListMultimap.create<UUID, Ticket>()

            DB.getResults("SELECT id, uuid, status, picker, location from puretickets_ticket WHERE status <> ?", TicketStatus.CLOSED.name)
                    .map { it.getUUID("uuid") to it.buildTicket() }
                    .forEach { (uuid, ticket) -> multimap.put(uuid, ticket) }

            return multimap
        }

        override fun select(id: Int): Ticket {
            val row = DB.getFirstRow("SELECT id, uuid, status, picker, location from puretickets_ticket WHERE id = ?", id)

            return row.buildTicket()
        }

        override fun selectAll(uuid: UUID, status: TicketStatus?): List<Ticket> {
            @Language("SQL")
            val sql = "SELECT id from puretickets_ticket WHERE uuid = ?"

            val results = if (status == null) {
                DB.getResults(sql, uuid.toString()
                )
            } else {
                DB.getResults(sql + " AND status = ?", uuid.toString(), status.name)
            }

            return results.map { it.buildTicket() }
        }

        override fun selectIds(uuid: UUID, status: TicketStatus?): List<Int> {
            @Language("SQL")
            val sql = "SELECT id, uuid, status, picker, location from puretickets_ticket WHERE uuid = ?"

            return if (status == null) {
                DB.getFirstColumnResults(sql, uuid.toString())
            } else {
                DB.getFirstColumnResults(sql + " AND status = ?", uuid.toString(), status.name)
            }
        }

        override fun selectHighestId(uuid: UUID, isActive: Boolean): Int? {
            val sql = "SELECT max(id) from puretickets_ticket WHERE uuid = ? AND status"

            return if (isActive) {
                DB.getFirstColumn(sql + "<> ?", uuid.toString(), TicketStatus.CLOSED.name)
            } else {
                DB.getFirstColumn(sql + "= ?", uuid.toString(), TicketStatus.CLOSED.name)
            }
        }

        override fun selectNames(status: TicketStatus?): List<String> {
            val row = if (status == null) {
                DB.getFirstColumnResults<String>("SELECT uuid from puretickets_ticket")
            } else {
                DB.getFirstColumnResults("SELECT uuid from puretickets_ticket WHERE status = ?", status.name)
            }

            return row.mapNotNull { UUID.fromString(it).asName() }
        }

        override fun selectTicketStats(uuid: UUID?): HashMap<TicketStatus, Int> {
            @Language("SQL")
            val sql = """
                SELECT
                    SUM(Status LIKE 'OPEN') AS open,
                    SUM(Status LIKE 'PICKED') AS picked,
                    SUM(status LIKE 'CLOSED') AS closed
                from puretickets_ticket
            """.trimIndent()

            val row = if (uuid != null) {
                DB.getFirstRow(sql + " WHERE uuid = ?", uuid.toString())
            } else {
                DB.getFirstRow(sql)
            }

            val results = HashMap<TicketStatus, Int>()

            results[TicketStatus.OPEN] = row.getInt("open")
            results[TicketStatus.PICKED] = row.getInt("picked")
            results[TicketStatus.CLOSED] = row.getInt("closed")

            return results
        }

        override fun exists(id: Int): Boolean {
            return DB.getFirstColumn<Long>("SELECT EXISTS(SELECT 1 from puretickets_ticket WHERE id = ?)", id) == 1.toLong()
        }

        override fun insert(ticket: Ticket) {
            DB.executeInsert("INSERT INTO puretickets_ticket(id, uuid, status, picker, location) VALUES(?, ?, ?, ?, ?)",
                    ticket.id, ticket.playerUUID.toString(), ticket.status.name, ticket.pickerUUID.toString(), ticket.location.serialized())
        }

        override fun update(ticket: Ticket) {
            DB.executeUpdateAsync("UPDATE puretickets_ticket SET status = ?, picker = ? WHERE id = ?",
                    ticket.status.name, ticket.pickerUUID.toString(), ticket.id)
        }
    }

    override val message = object : SQLManager.MessageFunctions {
        override fun selectAll(id: Int): ArrayList<Message> {
            val results = DB.getResults("SELECT reason, data, sender, date from puretickets_message WHERE ticket = ?", id)
                    .map { it.buildMessage() }

            return results as ArrayList<Message>
        }

        override fun insert(ticket: Ticket, message: Message) {
            DB.executeInsert("INSERT INTO puretickets_message(ticket, reason, data, sender, date) VALUES(?, ?, ?, ?, ?)",
                    ticket.id, message.reason.name, message.data, message.sender.toString(), message.date.serialized())
        }
    }

    override val notification = object : SQLManager.NotificationFunctions {
        override fun selectAllAndClear(): ArrayListMultimap<UUID, PendingNotification> {
            val multimap = ArrayListMultimap.create<UUID, PendingNotification>()

            DB.getResults("SELECT uuid, message, replacements from puretickets_notification")
                    .map { it.getUUID("uuid") to it.buildNotification() }
                    .forEach { (uuid, notification) -> multimap.put(uuid, notification) }

            DB.executeUpdate("DELETE from puretickets_notification")

            return multimap
        }

        override fun insertAll(notifications: ArrayListMultimap<UUID, PendingNotification>) {
            notifications.forEach { uuid, notification ->
                val message = notification.messageKey.name
                val replacements = notification.replacements.joinToString("|")

                DB.executeInsert("INSERT INTO puretickets_notification(uuid, message, replacements) VALUES(?, ?, ?)", uuid, message, replacements)
            }
        }
    }

    override val settings = object : SQLManager.SettingsFunctions {
        override fun select(uuid: UUID): UserSettings {
            val data = DB.getFirstRow("SELECT announcements from puretickets_settings WHERE uuid = ?", uuid.toString())
            val announcements = data.getString("announcements") == "1"

            return UserSettings(announcements)
        }

        override fun exists(uuid: UUID): Boolean {
            return DB.getFirstColumn<Long>("SELECT EXISTS(SELECT 1 from puretickets_settings WHERE uuid = ?)", uuid.toString()) == 1.toLong()
        }

        override fun insert(uuid: UUID, settings: UserSettings) {
            DB.executeInsert("INSERT INTO puretickets_settings(uuid, announcements) VALUES(?, ?)", uuid.toString(), settings.announcements)
        }

        override fun update(uuid: UUID, settings: UserSettings) {
            DB.executeUpdate("UPDATE puretickets_settings SET announcements = ? WHERE uuid = ?", settings.announcements, uuid.toString())
        }
    }

    override fun DbRow.getPureLong(column: String): Long {
        return getString(column).toLong()
    }
}