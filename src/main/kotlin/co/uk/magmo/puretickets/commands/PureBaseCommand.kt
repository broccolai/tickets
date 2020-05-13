package co.uk.magmo.puretickets.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.InvalidCommandArgument
import co.uk.magmo.puretickets.ticket.TicketInformation
import co.uk.magmo.puretickets.ticket.TicketManager
import org.bukkit.OfflinePlayer

open class PureBaseCommand : BaseCommand() {
    fun generateInformation(offlinePlayer: OfflinePlayer, index: Int?, closed: Boolean = false): TicketInformation {
        // todo: support closed
        if (index != null && !TicketManager[offlinePlayer.uniqueId].indices.contains(index - 1)) throw InvalidCommandArgument()
        return TicketInformation(offlinePlayer.uniqueId, if (index == null) 0 else index - 1)
    }
}