package co.uk.magmo.puretickets.ticket

import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.utils.asUUID
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID

object TicketManager {
    private val tickets = SQLFunctions.retrieveAllTickets()
    private var current = SQLFunctions.currentTicketId()

    operator fun get(uuid: UUID?, index: Int): Ticket? = tickets[uuid][index]

    operator fun get(uuid: UUID?): MutableList<Ticket> = tickets[uuid]

    operator fun contains(player: OfflinePlayer) = tickets.containsKey(player.uniqueId)

    fun asMap(): MutableMap<UUID, MutableCollection<Ticket>> = tickets.asMap()

    fun allKeys(): MutableSet<UUID> = tickets.keySet()

    fun createTicket(player: Player, message: Message) {
        Ticket(current.inc(), player.uniqueId, arrayListOf(message), TicketStatus.OPEN, null).also {
            tickets.put(player.uniqueId, it)

            SQLFunctions.insertTicket(it)
            SQLFunctions.insertMessage(it, message)
        }
    }

    fun pick(user: CommandSender, information: TicketInformation) {
        val ticket = tickets[information.player][information.index]
        val message = Message(MessageReason.PICKED, null, user.asUUID())

        ticket.status = TicketStatus.PICKED
        ticket.pickerUUID = user.asUUID()
        ticket.addMessageAndUpdate(message)

        tickets[information.player][information.index] = ticket
    }

    fun yield(user: CommandSender, information: TicketInformation) {
        val ticket = tickets[information.player][information.index]
        val message = Message(MessageReason.YIELDED, null, user.asUUID())

        ticket.status = TicketStatus.OPEN
        ticket.pickerUUID = null
        ticket.addMessageAndUpdate(message)

        tickets[information.player][information.index] = ticket
    }

    fun close(user: CommandSender, information: TicketInformation) {
        val ticket = tickets[information.player][information.index]
        val message = Message(MessageReason.CLOSED, null, user.asUUID())

        tickets.remove(information.player, ticket)
        ticket.status = TicketStatus.CLOSED
        ticket.addMessageAndUpdate(message)
    }

    fun done(user: CommandSender, information: TicketInformation) {
        val ticket = tickets[information.player][information.index]
        val message = Message(MessageReason.DONE_MARKED, null, user.asUUID())

        tickets.remove(information.player, ticket)
        ticket.status = TicketStatus.CLOSED
        ticket.addMessageAndUpdate(message)
    }

    fun reopen(sender: CommandSender, information: TicketInformation) {
        val ticket = SQLFunctions.retrieveSingleTicket(information.index)
        val message = Message(MessageReason.REOPENED, null, sender.asUUID())

        ticket.status = TicketStatus.OPEN
        ticket.addMessageAndUpdate(message)

        tickets.put(information.player, ticket)
    }

    private fun Ticket.addMessageAndUpdate(message: Message) {
        messages.add(message)
        SQLFunctions.insertMessage(this, message)
        SQLFunctions.updateTicket(this)
    }
}