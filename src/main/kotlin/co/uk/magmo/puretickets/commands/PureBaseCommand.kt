package co.uk.magmo.puretickets.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.HelpCommand
import co.uk.magmo.puretickets.exceptions.TicketNotFound
import co.uk.magmo.puretickets.interactions.NotificationManager
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.tasks.TaskManager
import co.uk.magmo.puretickets.ticket.MessageReason
import co.uk.magmo.puretickets.ticket.TicketInformation
import co.uk.magmo.puretickets.ticket.TicketManager
import co.uk.magmo.puretickets.ticket.TicketStatus
import co.uk.magmo.puretickets.utils.Utils
import co.uk.magmo.puretickets.utils.asName
import co.uk.magmo.puretickets.utils.formatted
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

open class PureBaseCommand : BaseCommand() {
    @Dependency
    protected lateinit var notificationManager: NotificationManager

    @Dependency
    protected lateinit var ticketManager: TicketManager

    @Dependency
    protected lateinit var taskManager: TaskManager

    @Default
    @HelpCommand
    fun onHelp(sender: CommandSender, help: CommandHelp) {
        help.helpEntries.removeAt(0)
        help.showHelp()
    }

    protected fun processShowCommand(issuer: CommandIssuer, information: TicketInformation) {
        taskManager {
            val ticket = ticketManager[information.player, information.index] ?: return@taskManager
            val replacements = Utils.ticketReplacements(ticket)
            val message = ticket.currentMessage()!!

            issuer.sendInfo(Messages.TITLES__SHOW_TICKET, *replacements)
            issuer.sendInfo(Messages.SHOW__SENDER, "%player%", ticket.playerUUID.asName(), "%date%", ticket.dateOpened().formatted())
            issuer.sendInfo(Messages.SHOW__MESSAGE, "%message%", message.data!!, "%date%", message.date.formatted())

            if (ticket.status != TicketStatus.PICKED) {
                issuer.sendInfo(Messages.SHOW__UNPICKED)
            } else {
                issuer.sendInfo(Messages.SHOW__PICKER, "%player%", ticket.pickerUUID.asName(),
                        "%date%", ticket.messages.last { it.reason == MessageReason.PICKED }.date.formatted())
            }
        }
    }

    protected fun processLogCommand(issuer: CommandIssuer, information: TicketInformation) {
        taskManager {
            val ticket = ticketManager[information.player, information.index] ?: return@taskManager
            val replacements = Utils.ticketReplacements(ticket)

            issuer.sendInfo(Messages.TITLES__TICKET_LOG, *replacements)

            ticket.messages.forEach {
                val suffix = it.data ?: it.sender.asName()
                issuer.sendInfo(Messages.GENERAL__LOG_FORMAT, "%reason%", it.reason.name, "%date%", it.date?.formatted(), "%suffix%", suffix)
            }
        }
    }

    protected fun generateInformation(offlinePlayer: OfflinePlayer, input: Int?, offline: Boolean): TicketInformation {
        var index = input

        if (index == null) {
            index = SQLFunctions.highestTicket(offlinePlayer.uniqueId, offline) ?: throw TicketNotFound()
        } else {
            if (!ticketManager[offlinePlayer.uniqueId].any { it.id == input }) {
                if (offline) {
                    if (!SQLFunctions.ticketExists(offlinePlayer.uniqueId, index)) throw TicketNotFound()
                } else {
                    throw TicketNotFound()
                }
            }
        }

        return TicketInformation(offlinePlayer.uniqueId, index)
    }
}