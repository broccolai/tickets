package co.uk.magmo.puretickets.ticket

import java.util.UUID

data class Ticket(val id: Int, val playerUUID: UUID, val messages: ArrayList<Message>, var status: TicketStatus, var pickerUUID: UUID?) {
    fun currentMessage() = messages.last { it.reason == MessageReason.MESSAGE }.data
}