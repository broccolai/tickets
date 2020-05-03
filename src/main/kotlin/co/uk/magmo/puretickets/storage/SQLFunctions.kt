package co.uk.magmo.puretickets.storage

import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS
import co.uk.magmo.puretickets.storage.tables.Messages
import co.uk.magmo.puretickets.storage.tables.Tickets
import co.uk.magmo.puretickets.ticket.Message
import co.uk.magmo.puretickets.ticket.MessageReason
import co.uk.magmo.puretickets.ticket.Ticket
import co.uk.magmo.puretickets.ticket.TicketStatus
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.max
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.support.sqlite.SQLiteDialect
import java.io.File
import java.util.UUID

object SQLFunctions {
    lateinit var database: Database

    fun setup() {
        val file = File(TICKETS.dataFolder, "tickets.db")
        file.createNewFile()

        database = Database.connect("jdbc:sqlite:$file", "org.sqlite.JDBC", null, null, SQLiteDialect())
    }

    fun currentTicketId() = database.from(Tickets).select(max(Tickets.id)).map { row -> row[Tickets.id] }.first() ?: 1

    fun insertTicket(ticket: Ticket) = database.insert(Tickets) {
        it.id to ticket.id
        it.uuid to ticket.playerUUID
        it.status to ticket.status.name
        it.picker to ticket.pickerUUID
    }

    fun insertMessage(ticket: Ticket, message: Message) = database.insert(Messages) {
        it.ticket to ticket.id
        it.reason to message.reason.name
        it.data to message.data
        it.sender to message.sender
        it.data to message.date
    }

    fun retrieveClosedTicketIds(uuid: UUID) = database.from(Tickets).select(Tickets.id)
        .where { (Tickets.uuid eq uuid) and (Tickets.status eq TicketStatus.CLOSED.name) }
        .map { queryRowSet -> queryRowSet[Tickets.id] }.toList()

    fun retrieveAllTickets() = database.from(Tickets).select()
        .where { Tickets.status eq TicketStatus.CLOSED.name }
        .map { row -> row.toTicket() }.toList()

    fun retrieveSingleTicket(id: Int) = database.from(Tickets).select()
        .where { Tickets.id eq id }
        .map { row -> row.toTicket() }.firstOrNull()

    // internal

    private fun retrieveMessages(id: Int) = database.from(Messages).select()
        .where { Messages.ticket eq id }
        .map { row -> row.toMessage() }.toList()

    private fun QueryRowSet.toTicket() =
        Ticket(get(Tickets.id)!!, get(Tickets.uuid)!!, ArrayList(retrieveMessages(get(Tickets.id)!!)),
            TicketStatus.valueOf(get(Tickets.status)!!), get(Tickets.picker))

    private fun QueryRowSet.toMessage() =
        Message(MessageReason.valueOf(get(Messages.reason)!!), get(Messages.data)!!, get(Messages.sender),
            get(Messages.date))
}