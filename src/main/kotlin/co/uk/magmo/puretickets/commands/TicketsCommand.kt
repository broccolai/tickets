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
    @CommandCompletion("locale")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reload")
    @Syntax("[reloadable]")
    fun onReload(issuer: CommandIssuer, @Single reloadable: String) {
        when (reloadable.toUpperCase()) {
            "LOCALE" -> {
                PureTickets.commandManager.loadLocales()
                issuer.sendMessage("Locale reloaded")
            }
            else -> {
                throw InvalidCommandArgument("Supplied argument is not a reloadable")
            }
        }
    }
}