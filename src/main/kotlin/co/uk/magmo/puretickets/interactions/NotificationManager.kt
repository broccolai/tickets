package co.uk.magmo.puretickets.interactions

import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.commands.CommandManager
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.user.UserManager
import co.uk.magmo.puretickets.utils.Constants
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.UUID

class NotificationManager(private val userManager: UserManager, private val commandManager: CommandManager) : Listener {
    private val awaiting = SQLFunctions.retrieveNotifications()

    fun reply(commandSender: CommandSender, messageKey: Messages, vararg replacements: String) {
        val ci = commandManager.getCommandIssuer(commandSender)
        ci.sendInfo(messageKey, *replacements)
    }

    fun send(uuid: UUID, messageKey: Messages, vararg replacements: String) {
        val op = Bukkit.getOfflinePlayer(uuid)

        if (op.isOnline) {
            val ci = commandManager.getCommandIssuer(op)
            ci.sendInfo(messageKey, *replacements)
        } else {
            awaiting.put(uuid, PendingNotification(messageKey, *replacements))
        }
    }

    fun announce(messageKey: Messages, vararg replacements: String) {
        Bukkit.getOnlinePlayers()
                .filter { it.hasPermission(Constants.STAFF_PERMISSION + ".announce") }
                .filter { userManager[it.uniqueId].announcements }
                .forEach { reply(it, messageKey, *replacements) }
    }

    fun save() {
        SQLFunctions.saveNotifications(awaiting)
    }

    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        val ci = commandManager.getCommandIssuer(player)

        awaiting[ci.uniqueId].forEach { n ->
            ci.sendInfo(n.messageKey, *n.replacements)
        }

        awaiting.removeAll(ci.uniqueId)
    }
}