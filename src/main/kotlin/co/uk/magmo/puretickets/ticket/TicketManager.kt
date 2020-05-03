package co.uk.magmo.puretickets.ticket

import co.uk.magmo.puretickets.storage.SQLFunctions
import com.google.common.collect.ArrayListMultimap
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID

object TicketManager {
    private val tickets = ArrayListMultimap.create<UUID, Ticket>()
    private var current = SQLFunctions.currentTicketId()

    operator fun get(uuid: UUID?, index: Int): Ticket? = tickets[uuid][index]

    operator fun get(uuid: UUID?): MutableList<Ticket> = tickets[uuid]

    operator fun contains(player: OfflinePlayer) = tickets.containsKey(player.uniqueId)

    fun all() = tickets.values()

    fun allKeys(): MutableSet<UUID> = tickets.keySet()

    fun add(uuid: UUID?, ticket: Ticket?) = tickets.put(uuid, ticket)

    fun remove(uuid: UUID?, ticket: Ticket?) = tickets.remove(uuid, ticket)

    fun countNot(status: TicketStatus) = tickets.values().filter { t: Ticket -> t.status != status }.count()

    fun createTicket(player: Player, message: Message) {
        Ticket(current.inc(), player.uniqueId, arrayListOf(message), TicketStatus.OPEN, null).also {
            tickets.put(player.uniqueId, it)

            SQLFunctions.insertTicket(it)
            SQLFunctions.insertMessage(it, message)
        }
    }

    fun Ticket.addMessage(message: Message) {
        messages.add(message)
        SQLFunctions.insertMessage(this, message)
    }

    fun pick(user: CommandSender, information: TicketInformation) {
        val uuid = if (user is Player) user.uniqueId else null
        val ticket = tickets[information.player][information.index]
        val message = Message(MessageReason.PICKED, null, uuid, null)

        ticket.status = TicketStatus.PICKED
        ticket.pickerUUID = uuid
        ticket.addMessage(message)

        tickets[information.player][information.index] = ticket
    }

    fun yield(user: CommandSender, information: TicketInformation) {
        // val ticket = tickets[information.player][information.index]
        // val message =
        //     Message(MessageReason.YIELDED, null, user.uuid, null)
        // ticket.status = TicketStatus.OPEN
        // addMessage(ticket, message)
        // tickets[information.player][information.index] = ticket
    }

    fun close(user: CommandSender, information: TicketInformation) {
        // val ticket = tickets[information.player][information.index]
        // val message =
        //     Message(MessageReason.CLOSED, null, user.uuid, null)
        // tickets.remove(information.player, ticket)
        // ticket.status = TicketStatus.CLOSED
        // addMessage(ticket, message)
    }

    fun done(user: CommandSender, information: TicketInformation) {
        // val ticket = tickets[information.player][information.index]
        // val message =
        //     Message(MessageReason.DONE_MARKED, null, user.uuid, null)
        // tickets.remove(information.player, ticket)
        // ticket.status = TicketStatus.CLOSED
        // addMessage(ticket, message)
    }
    //    public void reopen(CommandSender sender, TicketInformation information) {
//        Ticket ticket = tickets.get(information.player).get(information.index);
//        Message message = new Message(MessageReason.REOPENED, null, sender, null);
//
//        ticket.status = TicketStatus.OPEN;
//        addMessage(ticket, message);
//    }
}