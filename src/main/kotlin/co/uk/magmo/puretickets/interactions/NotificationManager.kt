package co.uk.magmo.puretickets.interactions

import co.aikar.commands.CommandIssuer
import co.aikar.commands.MessageType
import co.uk.magmo.puretickets.commands.CommandManager
import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.integrations.DiscordManager
import co.uk.magmo.puretickets.locale.MessageNames
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.locale.TargetType.*
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.tasks.ReminderTask
import co.uk.magmo.puretickets.tasks.TaskManager
import co.uk.magmo.puretickets.ticket.Ticket
import co.uk.magmo.puretickets.ticket.TicketManager
import co.uk.magmo.puretickets.user.UserManager
import co.uk.magmo.puretickets.utils.Constants
import co.uk.magmo.puretickets.utils.Utils
import co.uk.magmo.puretickets.utils.asName
import co.uk.magmo.puretickets.utils.minuteToTick
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.permissions.ServerOperator
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.filter
import kotlin.collections.forEach

class NotificationManager(private val userManager: UserManager, private val commandManager: CommandManager, private val discordManager: DiscordManager, ticketManager: TicketManager, taskManager: TaskManager) : Listener {
    private val awaiting = SQLFunctions.retrieveNotifications()

    init {
        taskManager.addRepeatingTask(ReminderTask(ticketManager, this),
                Config.reminderDelay.minuteToTick(), Config.reminderRepeat.minuteToTick())
    }

    fun ServerOperator.asIssuer(): CommandIssuer = commandManager.getCommandIssuer(this)

    // TODO: Needs to be made more generic
    fun send(sender: CommandSender, target: UUID?, names: MessageNames, ticket: Ticket, addFields: ((HashMap<String, String>) -> Unit) = {}) {
        val replacements = arrayOf("%user%", sender.name, "%target%", target.asName(), *Utils.ticketReplacements(ticket))

        names.targets.forEach { targetType ->
            val message = Messages.retrieve(targetType, names)

            when (targetType) {
                SENDER -> {
                    sender.asIssuer().sendInfo(message, *replacements)
                }

                NOTIFICATION -> {
                    val op = Bukkit.getOfflinePlayer(target!!)

                    if (op.isOnline) {
                        op.asIssuer().sendInfo(message, *replacements)
                    } else {
                        awaiting.put(target, PendingNotification(message, *replacements))
                    }
                }

                ANNOUNCEMENT -> {
                    Bukkit.getOnlinePlayers()
                            .filter { it.hasPermission(Constants.STAFF_PERMISSION + ".announce") }
                            .filter { userManager[it.uniqueId].announcements }
                            .filter { if (sender is Player) sender.uniqueId != it.uniqueId else true }
                            .forEach { it.asIssuer().sendInfo(message, *replacements) }
                }

                DISCORD -> {
                    val fields = HashMap<String, String>().also(addFields)

                    var action = commandManager.formatMessage(Bukkit.getConsoleSender().asIssuer(), MessageType.INFO, message)
                    action = ChatColor.stripColor(action)

                    discordManager.sendInformation(ticket.status.color.hexColor, ticket.playerUUID.asName(), ticket.id, action, fields)
                }
            }
        }
    }

    fun basic(commandSender: CommandSender, messageKey: Messages, vararg replacements: String) {
        commandSender.asIssuer().sendInfo(messageKey, *replacements)
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