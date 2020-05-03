package co.uk.magmo.puretickets.commands

import co.aikar.commands.CommandIssuer
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Single
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.annotation.Syntax
import co.uk.magmo.puretickets.PureTickets
import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS

@CommandAlias("tickets")
class TicketsCommand : PureBaseCommand() {
    @Subcommand("info")
    fun onInfo(issuer: CommandIssuer) {
        issuer.sendMessage(TICKETS.name + " " + TICKETS.description.version)
    }

    @Subcommand("reload")
    @CommandCompletion("locale")
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