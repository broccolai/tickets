package co.uk.magmo.puretickets.commands

import co.aikar.commands.CommandIssuer
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.annotation.*
import co.uk.magmo.puretickets.PureTickets
import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS
import co.uk.magmo.puretickets.utils.Constants

@CommandAlias("tickets")
class TicketsCommand : PureBaseCommand() {
    @Default
    @Subcommand("info")
    fun onInfo(issuer: CommandIssuer) {
        issuer.sendMessage(TICKETS.name + " " + TICKETS.description.version)
    }

    @Subcommand("reload")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reload")
    fun onReload(issuer: CommandIssuer) {
        TICKETS.onDisable()
        TICKETS.onEnable()
        issuer.sendMessage("PureTickets reloaded")
    }
}