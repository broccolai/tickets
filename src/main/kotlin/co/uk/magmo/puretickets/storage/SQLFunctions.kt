package co.uk.magmo.puretickets.storage

import co.aikar.idb.*
import co.uk.magmo.puretickets.interactions.PendingNotification
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.ticket.Message
import co.uk.magmo.puretickets.ticket.MessageReason
import co.uk.magmo.puretickets.ticket.Ticket
import co.uk.magmo.puretickets.ticket.TicketStatus
import co.uk.magmo.puretickets.user.UserSettings
import co.uk.magmo.puretickets.utils.asName
import com.google.common.collect.ArrayListMultimap
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.Plugin
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.logging.Level

object SQLFunctions {
    fun setup(plugin: Plugin) {
        val file = File(plugin.dataFolder, "tickets.db")
        file.createNewFile()

        val options = DatabaseOptions.builder().sqlite(file.toString()).build()
        val database = PooledDatabaseOptions.builder().options(options).createHikariDatabase()

        DB.setGlobalDatabase(database)
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS ticket(id INTEGER, uuid TEXT, status TEXT, picker TEXT)")
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS message(ticket INTEGER, reason TEXT, data TEXT, sender TEXT, date TEXT)")
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS notification(uuid TEXT, message TEXT, replacements TEXT)")
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS settings(uuid TEXT, announcements TEXT)")

        var version = DB.getFirstColumn<Int>("PRAGMA user_version")

        if (version == 0) {
            plugin.logger.log(Level.INFO, "Updated PureTickets database to have location column")
            DB.executeUpdate("ALTER TABLE ticket ADD location TEXT")
            version++
        }

        if (version <= 1) {
            plugin.logger.log(Level.INFO, "Updated PureTickets database to remove tickets with empty locations and remove all pending notifications")
            DB.executeUpdate("DELETE FROM ticket WHERE location IS NULL OR trim(location) = ?", "")
            DB.executeUpdate("DELETE FROM notification")
            version++
        }

        DB.executeUpdate("PRAGMA user_version = $version")
    }

    fun settingsExist(uuid: UUID) = DB.getFirstColumn<Int>("SELECT EXISTS(SELECT 1 FROM settings WHERE uuid = ?)", uuid) == 1

    fun retrieveUserSettings(uuid: UUID): UserSettings {
        val data = DB.getFirstRow("SELECT announcements FROM settings WHERE uuid = ?", uuid)
        val announcements = data.getString("announcements") == "1"

        return UserSettings(announcements)
    }

    fun insertUserSettings(uuid: UUID, settings: UserSettings): Long = DB.executeInsert("INSERT INTO settings(uuid, announcements) VALUES(?, ?)",
        uuid, settings)

    fun updateUserSettings(uuid: UUID, settings: UserSettings) = DB.executeUpdate("UPDATE settings SET announcements = ? WHERE uuid = ?",
        settings.announcements, uuid)

    fun retrieveNotifications(): ArrayListMultimap<UUID, PendingNotification> = ArrayListMultimap.create<UUID, PendingNotification>().apply {
        DB.getResults("SELECT uuid, message, replacements FROM notification")
                .map { row -> row.getUUID("uuid") to PendingNotification(Messages.valueOf(row.getString("message")),
                        *row.getString("replacements").split("|").toTypedArray()) }
                .forEach { put(it.first, it.second) }

        DB.executeUpdate("DELETE FROM notification")
    }

    fun saveNotifications(notifications: ArrayListMultimap<UUID, PendingNotification>) {
        notifications.forEach { uuid, notification ->
            val message = notification.messageKey.name
            val replacements = notification.replacements.joinToString("|")

            DB.executeInsert("INSERT INTO notification(uuid, message, replacements) VALUES(?, ?, ?)", uuid, message, replacements)
        }
    }

    fun currentTicketId() = DB.getFirstColumn<Int>("SELECT max(id) FROM ticket") ?: 0

    fun insertTicket(t: Ticket): Long = DB.executeInsert("INSERT INTO ticket(id, uuid, status, picker, location) VALUES(?, ?, ?, ?, ?)",
            t.id, t.playerUUID, t.status.name, t.pickerUUID, t.location.save())

    fun updateTicket(t: Ticket): CompletableFuture<Int> = DB.executeUpdateAsync("UPDATE ticket SET status = ?, picker = ? WHERE id = ?",
            t.status.name, t.pickerUUID, t.id)

    fun insertMessage(t: Ticket, m: Message): Long = DB.executeInsert("INSERT INTO message(ticket, reason, data, sender, date) VALUES(?, ?, ?, ?, ?)",
            t.id, m.reason.name, m.data, m.sender, m.date?.atZone(ZoneId.systemDefault())?.toEpochSecond())

    fun retrieveTicketNames(): List<String> = DB.getFirstColumnResults<String>("SELECT uuid FROM ticket")
            .mapNotNull { UUID.fromString(it) }
            .map { it.asName() }

    fun retrieveClosedTicketNames(): List<String> = DB.getFirstColumnResults<String>("SELECT uuid FROM ticket WHERE status = ?", TicketStatus.CLOSED)
            .mapNotNull { UUID.fromString(it) }
            .map { it.asName() }

    fun retrieveClosedTickets(uuid: UUID): List<Ticket> = DB.getResults("SELECT id, uuid, status, picker, location FROM ticket WHERE uuid = ?", uuid)
            .map { it.toTicket() }

    fun retrieveClosedTicketIds(uuid: UUID): List<Int> = DB.getFirstColumnResults("SELECT id FROM ticket WHERE uuid = ? AND status = ?",
            uuid, TicketStatus.CLOSED.name)

    fun retrieveAllTickets(): ArrayListMultimap<UUID, Ticket> = ArrayListMultimap.create<UUID, Ticket>().apply {
        DB.getResults("SELECT id, uuid, status, picker, location FROM ticket WHERE status <> ?", TicketStatus.CLOSED.name)
                .map { row -> row.getUUID("uuid") to row.toTicket() }
                .forEach { put(it.first, it.second) }
    }

    fun retrieveSingleTicket(id: Int) = DB.getFirstRow("SELECT * FROM ticket WHERE id = ?", id).toTicket()

    fun ticketExists(uuid: UUID, id: Int): Boolean = DB.getFirstColumn<Int>("SELECT EXISTS(SELECT 1 FROM ticket WHERE uuid = ? AND id = ?)", uuid, id) == 1

    fun highestTicket(uuid: UUID, closed: Boolean): Int? {
        var sql = "SELECT max(id) FROM ticket WHERE uuid = ? AND status"

        sql += if (closed) " = ?" else " <> ?"

        return DB.getFirstColumn(sql, uuid, TicketStatus.CLOSED.name)
    }

    fun selectCurrentTickets(uuid: UUID?): HashMap<TicketStatus, Int> {
        val sql = """
            SELECT
                SUM(Status LIKE 'OPEN') AS open,
                SUM(Status LIKE 'PICKED') AS picked,
                SUM(status LIKE 'CLOSED') AS closed
            FROM ticket
        """.trimIndent()

        val row = if (uuid != null) {
            DB.getFirstRow(sql + " WHERE uuid = ?", uuid)
        } else {
            DB.getFirstRow(sql)
        }

        val results = HashMap<TicketStatus, Int>()

        results[TicketStatus.OPEN] = row.getInt("open")
        results[TicketStatus.PICKED] = row.getInt("picked")
        results[TicketStatus.CLOSED] = row.getInt("closed")

        return results
}

    private fun retrieveMessages(id: Int) = DB.getResults("SELECT reason, data, sender, date FROM message WHERE ticket = ?", id)
            .map { row -> row.toMessage() } as ArrayList<Message>

    private fun Location.save() = world?.name + "|" + blockX + "|" + blockY + "|" + blockZ

    private fun DbRow.getLocation(column: String): Location {
        val split = getString(column)?.split("|")!!
        val world = Bukkit.getWorld(split[0])

        return Location(world, split[1].toDouble(), split[2].toDouble(), split[3].toDouble())
    }

    private fun DbRow.getUUID(column: String): UUID? {
        val raw = getString(column)

        return if (raw != null) UUID.fromString(raw) else null
    }

    private fun DbRow.getLiteLong(column: String) = getString(column).toLong()

    private fun DbRow.toTicket(): Ticket {
        val id = getInt("id")

        return Ticket(id, getUUID("uuid")!!, retrieveMessages(id), TicketStatus.valueOf(getString("status")), getUUID("picker"), getLocation("location"))
    }

    private fun DbRow.toMessage() =
        Message(MessageReason.valueOf(getString("reason")), getString("data"), getUUID("sender"),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(getLiteLong("date")), ZoneId.systemDefault()))
}