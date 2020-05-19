package co.uk.magmo.puretickets.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.InvalidCommandArgument
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.ticket.TicketInformation
import co.uk.magmo.puretickets.ticket.TicketManager
import org.bukkit.OfflinePlayer

open class PureBaseCommand : BaseCommand() {
    fun generateInformation(offlinePlayer: OfflinePlayer, input: Int?, offline: Boolean): TicketInformation {
        var index = input

        if (index == null) {
            index = SQLFunctions.highestTicket(offlinePlayer.uniqueId, offline) ?: throw InvalidCommandArgument()
        } else {
            if (!TicketManager[offlinePlayer.uniqueId].any { it.id == input }) {
                if (offline) {
                    if (!SQLFunctions.ticketExists(offlinePlayer.uniqueId, index)) throw InvalidCommandArgument()
                } else {
                    throw InvalidCommandArgument()
                }
            }
        }

        return TicketInformation(offlinePlayer.uniqueId, index)
    }
}