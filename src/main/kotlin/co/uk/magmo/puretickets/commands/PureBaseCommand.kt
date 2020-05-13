package co.uk.magmo.puretickets.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.InvalidCommandArgument
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.ticket.TicketInformation
import co.uk.magmo.puretickets.ticket.TicketManager
import org.bukkit.OfflinePlayer

open class PureBaseCommand : BaseCommand() {
    fun generateInformation(offlinePlayer: OfflinePlayer, input: Int?, closed: Boolean = false): TicketInformation {
        var index = if (input != null) input - 1 else null

        if (closed) {
            if (index == null)
                index = SQLFunctions.highestTicket(offlinePlayer.uniqueId) ?: throw InvalidCommandArgument()

            if (!SQLFunctions.ticketExists(offlinePlayer.uniqueId, index)) throw InvalidCommandArgument()
        } else if (index != null && !TicketManager[offlinePlayer.uniqueId].indices.contains(index)) {
            throw InvalidCommandArgument()
        }

        return TicketInformation(offlinePlayer.uniqueId, index ?: 0)
    }
}