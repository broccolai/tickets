package co.uk.magmo.puretickets.storage

import co.aikar.idb.*
import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS
import co.uk.magmo.puretickets.ticket.Message
import co.uk.magmo.puretickets.ticket.MessageReason
import co.uk.magmo.puretickets.ticket.Ticket
import co.uk.magmo.puretickets.ticket.TicketStatus
import co.uk.magmo.puretickets.utils.asName
import com.google.common.collect.ArrayListMultimap
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

object SQLFunctions {
    fun setup() {
        val file = File(TICKETS.dataFolder, "tickets.db")
        file.createNewFile()

        val options = DatabaseOptions.builder().sqlite(file.toString()).build()
        val database = PooledDatabaseOptions.builder().options(options).createHikariDatabase()

        DB.setGlobalDatabase(database)
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS ticket(id INTEGER, uuid TEXT, status TEXT, picker TEXT)")
        DB.executeUpdate("CREATE TABLE IF NOT EXISTS message(ticket INTEGER, reason TEXT, data TEXT, sender TEXT, date TEXT)")
    }

    fun currentTicketId() = DB.getFirstColumn<Int>("SELECT max(id) FROM ticket") ?: 0

    fun insertTicket(t: Ticket): Long = DB.executeInsert("INSERT INTO ticket(id, uuid, status, picker) VALUES(?, ?, ?, ?)",
            t.id, t.playerUUID, t.status.name, t.pickerUUID)

    fun updateTicket(t: Ticket) = DB.executeUpdateAsync("UPDATE ticket SET status = ?, picker = ? WHERE id = ?",
            t.status.name, t.pickerUUID, t.id)

    fun insertMessage(t: Ticket, m: Message): Long = DB.executeInsert("INSERT INTO message(ticket, reason, data, sender, date) VALUES(?, ?, ?, ?, ?)",
            t.id, m.reason.name, m.data, m.sender, m.date?.atZone(ZoneId.systemDefault())?.toEpochSecond())

    fun retrieveTicketNames(): List<String> = DB.getFirstColumnResults<String>("SELECT uuid FROM ticket")
            .mapNotNull { UUID.fromString(it) }
            .map { it.asName() }

    fun retrieveClosedTicketNames(): List<String> = DB.getFirstColumnResults<String>("SELECT uuid FROM ticket WHERE status = ?", TicketStatus.CLOSED)
            .mapNotNull { UUID.fromString(it) }
            .map { it.asName() }

    fun retrieveClosedTickets(uuid: UUID): List<Ticket> = DB.getResults("SELECT id, uuid, status, picker FROM ticket WHERE uuid = ?", uuid)
            .map { it.toTicket() }

    fun retrieveClosedTicketIds(uuid: UUID): List<Int> = DB.getFirstColumnResults("SELECT id FROM ticket WHERE uuid = ? AND status = ?",
            uuid, TicketStatus.CLOSED.name)

    fun retrieveAllTickets(): ArrayListMultimap<UUID, Ticket> = ArrayListMultimap.create<UUID, Ticket>().apply {
        DB.getResults("SELECT id, uuid, status, picker FROM ticket WHERE status <> ?", TicketStatus.CLOSED.name)
                .map { row -> row.getUUID("uuid") to row.toTicket() }
                .forEach { put(it.first, it.second) }
    }

    fun retrieveSingleTicket(id: Int) = DB.getFirstRow("SELECT * FROM ticket WHERE id = ?", id).toTicket()

    fun ticketExists(uuid: UUID, id: Int): Boolean = DB.getFirstColumn<Int>("SELECT EXISTS(SELECT 1 FROM ticket WHERE uuid = ? AND id = ?)", uuid, id) == 1

    fun highestTicket(uuid: UUID): Int? = DB.getFirstColumn("SELECT max(id) FROM ticket WHERE uuid = ?", uuid)

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

    private fun DbRow.getUUID(column: String): UUID? {
        val raw = getString(column)

        return if (raw != null) UUID.fromString(raw) else null
    }

    private fun DbRow.getLiteLong(column: String) = getString(column).toLong()

    private fun DbRow.toTicket(): Ticket {
        val id = getInt("id")

        return Ticket(id, getUUID("uuid")!!, retrieveMessages(id), TicketStatus.valueOf(getString("status")), getUUID("picker"))
    }

    private fun DbRow.toMessage() =
        Message(MessageReason.valueOf(getString("reason")), getString("data"), getUUID("sender"),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(getLiteLong("date")), ZoneId.systemDefault()))
}