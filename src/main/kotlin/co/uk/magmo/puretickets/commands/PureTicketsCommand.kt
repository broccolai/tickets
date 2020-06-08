package co.uk.magmo.puretickets.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.*
import co.uk.magmo.puretickets.PureTickets
import co.uk.magmo.puretickets.exceptions.InvalidSettingType
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.user.UserManager
import co.uk.magmo.puretickets.utils.Constants
import org.bukkit.entity.Player

@CommandAlias("puretickets")
class PureTicketsCommand : BaseCommand() {
    @Dependency
    private lateinit var plugin: PureTickets
    @Dependency
    private lateinit var userManager: UserManager

    @Default
    @Subcommand("info")
    fun onInfo(issuer: CommandIssuer) {
        issuer.sendMessage(name + " " + plugin.description.version)
    }

    @Subcommand("settings")
    @CommandPermission(Constants.USER_PERMISSION + ".settings")
    @CommandCompletion("announcements true|false")
    fun onSettings(player: Player, setting: String, @Optional value: Boolean) {
        userManager.update(player.uniqueId) { settings ->
            when(setting.toLowerCase()) {
                "announcements" -> settings.announcements = value

                else -> throw InvalidSettingType()
            }
        }

        val status = if (value) "enabled" else "disabled"
        currentCommandIssuer.sendInfo(Messages.OTHER__SETTING_UPDATE, "%setting%", setting, "%status%", status)
    }

    @Subcommand("reload")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reload")
    fun onReload(issuer: CommandIssuer) {
        plugin.onDisable()
        plugin.onEnable()
        issuer.sendMessage("PureTickets reloaded")
    }
}