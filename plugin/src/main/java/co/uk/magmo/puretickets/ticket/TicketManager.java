package co.uk.magmo.puretickets.ticket;

import co.uk.magmo.puretickets.storage.SQLManager;
import co.uk.magmo.puretickets.storage.TimeAmount;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

class TicketManager {
    private SQLManager sqlManager;

    public TicketManager(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public @Nullable Ticket get(int id) {
        return sqlManager.getTicket().select(id);
    }

    operator fun get(uuid: UUID, status: TicketStatus? = null) = sqlManager.ticket.selectAll(uuid, status)

    fun getIds(uuid: UUID, status: TicketStatus? = null) = sqlManager.ticket.selectIds(uuid, status)

    fun exists(id: Int) = sqlManager.ticket.exists(id)

    fun count(status: TicketStatus? = null) = sqlManager.ticket.count(status)

    fun stats(uuid: UUID? = null) = sqlManager.ticket.selectTicketStats(uuid)

    fun all(status: TicketStatus? = null) = sqlManager.ticket.selectAll(status)

    fun allNames(status: TicketStatus? = null) = sqlManager.ticket.selectNames(status)

    fun getHighest(uuid: UUID, vararg status: TicketStatus) = sqlManager.ticket.selectHighestId(uuid, *status)

    fun highscores(amount: TimeAmount) = sqlManager.highscores(amount)

    fun createTicket(player: Player, message: Message): Ticket {
        val id = sqlManager.ticket.insert(player.uniqueId, TicketStatus.OPEN, null, player.location)
        val ticket = Ticket(id, player.uniqueId, arrayListOf(message), TicketStatus.OPEN, null, player.location)

        sqlManager.message.insert(ticket, message)

        return ticket
    }

    fun update(id: Int, message: Message): Ticket? {
        val ticket = get(id) ?: return null

        ticket.addMessageAndUpdate(message)

        return ticket
    }

    fun pick(uuid: UUID?, id: Int): Ticket? {
        val ticket = get(id) ?: return null
        val message = Message(MessageReason.PICKED, null, uuid)

        ticket.status = TicketStatus.PICKED
        ticket.pickerUUID = uuid
        ticket.addMessageAndUpdate(message)

        return ticket
    }

    fun yield(uuid: UUID?, id: Int): Ticket? {
        val ticket = get(id) ?: return null
        val message = Message(MessageReason.YIELDED, null, uuid)

        ticket.status = TicketStatus.OPEN
        ticket.pickerUUID = null
        ticket.addMessageAndUpdate(message)

        return ticket
    }

    fun close(uuid: UUID?, id: Int): Ticket? {
        val ticket = get(id) ?: return null
        val message = Message(MessageReason.CLOSED, null, uuid)

        ticket.status = TicketStatus.CLOSED
        ticket.addMessageAndUpdate(message)

        return ticket
    }

    fun done(uuid: UUID?, id: Int): Ticket? {
        val ticket = get(id) ?: return null
        val message = Message(MessageReason.DONE_MARKED, null, uuid)

        ticket.status = TicketStatus.CLOSED
        ticket.addMessageAndUpdate(message)

        return ticket
    }

    fun reopen(uuid: UUID?, id: Int): Ticket? {
        val ticket = get(id) ?: return null
        val message = Message(MessageReason.REOPENED, null, uuid)

        ticket.status = TicketStatus.OPEN
        ticket.addMessageAndUpdate(message)

        return ticket
    }

    fun note(uuid: UUID?, id: Int, input: String): Ticket? {
        val ticket = get(id) ?: return null
        val message = Message(MessageReason.NOTE, input, uuid)

        ticket.addMessageAndUpdate(message)

        return ticket
    }

    private fun Ticket.addMessageAndUpdate(message: Message) {
        messages.add(message)

        sqlManager.message.insert(this, message)
        sqlManager.ticket.update(this)
    }
}