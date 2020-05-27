package co.uk.magmo.puretickets.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.annotation.*
import co.uk.magmo.puretickets.PureTickets
import co.uk.magmo.puretickets.interactions.NotificationManager
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.user.UserManager
import co.uk.magmo.puretickets.utils.Constants
import org.bukkit.entity.Player

@CommandAlias("tickets")
class TicketsCommand : BaseCommand() {
    @Dependency
    private lateinit var plugin: PureTickets
    @Dependency
    private lateinit var userManager: UserManager
    @Dependency
    private lateinit var notificationManager: NotificationManager

    @Default
    @Subcommand("info")
    fun onInfo(issuer: CommandIssuer) {
        issuer.sendMessage(name + " " + plugin.description.version)
    }

    @Subcommand("settings")
    @CommandPermission(Constants.USER_PERMISSION + ".settings")
    fun onSettings(player: Player, setting: String, value: Boolean) {
        userManager.update(player.uniqueId) { settings ->
            when(setting.toLowerCase()) {
                "announcements" -> settings.announcements = value

                else -> throw InvalidCommandArgument("Provided setting is not an option")
            }
        }

        val status = if (value) "enabled" else "disabled"
        notificationManager.reply(player, Messages.FORMAT__SETTING_UPDATE, "%setting%", setting, "%status%", status)
    }

    @Subcommand("reload")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reload")
    fun onReload(issuer: CommandIssuer) {
        plugin.onDisable()
        plugin.onEnable()
        issuer.sendMessage("PureTickets reloaded")
    }
}