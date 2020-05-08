package co.uk.magmo.puretickets.storage

import co.aikar.idb.*
import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS
import co.uk.magmo.puretickets.ticket.Message
import co.uk.magmo.puretickets.ticket.MessageReason
import co.uk.magmo.puretickets.ticket.Ticket
import co.uk.magmo.puretickets.ticket.TicketStatus
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
            t.id, t.playerUUID, t.status, t.pickerUUID)

    fun insertMessage(t: Ticket, m: Message): Long = DB.executeInsert("INSERT INTO message(ticket, reason, data, sender, date) VALUES(?, ?, ?, ?, ?)",
            t.id, m.reason.name, m.data, m.sender, m.date?.atZone(ZoneId.systemDefault())?.toEpochSecond())

    fun retrieveClosedTicketIds(uuid: UUID): List<Int> = DB.getFirstColumnResults("SELECT id FROM ticket WHERE uuid = ? AND status = ?",
            uuid, TicketStatus.CLOSED.name)

    fun retrieveAllTickets(): ArrayListMultimap<UUID, Ticket> = ArrayListMultimap.create<UUID, Ticket>().apply {
        DB.getResults("SELECT id, uuid, status, picker FROM ticket WHERE status <> ?", TicketStatus.CLOSED.name)
                .map { row -> row.getUUID("uuid") to row.toTicket() }
                .forEach { put(it.first, it.second) }
    }

    fun retrieveSingleTicket(id: Int) = DB.getFirstRow("SELECT * FROM ticket WHERE id = ?", id).toTicket()

    private fun retrieveMessages(id: Int) = DB.getResults("SELECT reason, data, sender, date FROM message WHERE ticket = ?", id)
            .map { row -> row.toMessage() } as ArrayList<Message>

    private fun DbRow.getUUID(column: String): UUID? {
        val raw = getString(column)

        return if (raw != null) UUID.fromString(raw) else null
    }

    private fun DbRow.toTicket(): Ticket {
        val id = getInt("id")

        return Ticket(id, getUUID("uuid")!!, retrieveMessages(id), TicketStatus.valueOf(getString("status")), getUUID("picker"))
    }

    private fun DbRow.toMessage() =
        Message(MessageReason.valueOf(getString("reason")), getString("data"), getUUID("sender"),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(getLong("date")), ZoneId.systemDefault()))
}