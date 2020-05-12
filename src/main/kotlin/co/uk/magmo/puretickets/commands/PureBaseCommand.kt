package co.uk.magmo.puretickets.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.InvalidCommandArgument
import co.uk.magmo.puretickets.ticket.TicketInformation
import co.uk.magmo.puretickets.ticket.TicketManager
import org.bukkit.OfflinePlayer

open class PureBaseCommand : BaseCommand() {
    fun generateInformation(offlinePlayer: OfflinePlayer, index: Int?): TicketInformation {
        if (index != null && !TicketManager[offlinePlayer.uniqueId].indices.contains(index)) throw InvalidCommandArgument()
        return TicketInformation(offlinePlayer.uniqueId, if (index == null) 0 else index - 1)
    }
}