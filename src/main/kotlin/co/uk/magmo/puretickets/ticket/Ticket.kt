package co.uk.magmo.puretickets.ticket

import org.bukkit.Location
import java.util.UUID

data class Ticket(val id: Int, val playerUUID: UUID, val messages: ArrayList<Message>, var status: TicketStatus, var pickerUUID: UUID?, val location: Location?) {
    fun currentMessage() = messages.lastOrNull { it.reason == MessageReason.MESSAGE }

    fun dateOpened() = messages.first { it.reason == MessageReason.MESSAGE }.date
}