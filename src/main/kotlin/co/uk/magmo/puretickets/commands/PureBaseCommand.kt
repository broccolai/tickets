package co.uk.magmo.puretickets.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.InvalidCommandArgument
import co.uk.magmo.puretickets.exceptions.InvalidIssuerException
import co.uk.magmo.puretickets.ticket.TicketInformation
import co.uk.magmo.puretickets.ticket.TicketManager
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

open class PureBaseCommand : BaseCommand() {
    fun ensurePlayer(issuer: CommandIssuer): Player {
        if (!issuer.isPlayer) throw InvalidIssuerException()
        return issuer.getIssuer()
    }

    fun generateInformation(offlinePlayer: OfflinePlayer, index: Int?): TicketInformation {
        if (index != null && index > TicketManager[offlinePlayer.uniqueId].size) throw InvalidCommandArgument()
        return TicketInformation(offlinePlayer.uniqueId, if (index == null) 0 else index - 1)
    }
}