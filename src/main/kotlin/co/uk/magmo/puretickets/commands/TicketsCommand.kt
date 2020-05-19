package co.uk.magmo.puretickets.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.*
import co.uk.magmo.puretickets.PureTickets
import co.uk.magmo.puretickets.utils.Constants

@CommandAlias("tickets")
class TicketsCommand : BaseCommand() {
    @Dependency
    private lateinit var plugin: PureTickets

    @Default
    @Subcommand("info")
    fun onInfo(issuer: CommandIssuer) {
        issuer.sendMessage(name + " " + plugin.description.version)
    }

    @Subcommand("reload")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reload")
    fun onReload(issuer: CommandIssuer) {
        plugin.onDisable()
        plugin.onEnable()
        issuer.sendMessage("PureTickets reloaded")
    }
}