package co.uk.magmo.puretickets.commands

import co.aikar.commands.annotation.*
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.ticket.*
import co.uk.magmo.puretickets.utils.*
import com.okkero.skedule.SynchronizationContext
import org.bukkit.entity.Player

@CommandAlias("ticket|ti")
@CommandPermission(Constants.USER_PERMISSION)
class TicketCommand : PureBaseCommand() {
    @Subcommand("%create")
    @CommandPermission(Constants.USER_PERMISSION + ".create")
    @Description("Create a ticket")
    @Syntax("<Message>")
    fun onCreate(player: Player, message: Message) {
        taskManager {
            val ticket = ticketManager.createTicket(player, message)
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(player, Messages.TICKET__CREATED, *replacements)
            notificationManager.announce(Messages.ANNOUNCEMENTS__NEW_TICKET, "%user%", player.name, *replacements)
        }
    }

    @Subcommand("%update")
    @CommandCompletion("@IssuerTicketIds")
    @CommandPermission(Constants.USER_PERMISSION + ".update")
    @Description("Update a ticket")
    @Syntax("<Index> <Message>")
    fun onUpdate(player: Player, index: Int, message: Message) {
        val information = generateInformation(player, index, false)

        taskManager {
            val ticket = ticketManager.update(information, message)
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(player, Messages.TICKET__UPDATED, *replacements)
            notificationManager.announce(Messages.ANNOUNCEMENTS__UPDATED_TICKET, "%user%", player.name, *replacements)
        }
    }

    @Subcommand("%close")
    @CommandCompletion("@IssuerTicketIds")
    @CommandPermission(Constants.USER_PERMISSION + ".close")
    @Description("Close a ticket")
    @Syntax("[Index]")
    fun onClose(player: Player, @Optional index: Int?) {
        val information = generateInformation(player, index, false)

        taskManager {
            val ticket = ticketManager.close(player.asUUID(), information)
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(player, Messages.TICKET__CLOSED, *replacements)
            notificationManager.announce(Messages.ANNOUNCEMENTS__CLOSED_TICKET, "%user%", player.name, *replacements)
        }
    }

    @Subcommand("%show")
    @CommandCompletion("@IssuerTicketIds")
    @CommandPermission(Constants.USER_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("[Index]")
    fun onShow(player: Player, @Optional index: Int?) {
        val information = generateInformation(player, index, false)

        processShowCommand(player, information)
    }

    @Subcommand("%list")
    @CommandCompletion("@TicketStatus")
    @CommandPermission(Constants.USER_PERMISSION + ".list")
    @Description("List all tickets")
    @Syntax("[Status]")
    fun onList(player: Player, @Optional status: TicketStatus?) {
        var tickets = ticketManager[player.uniqueId]

        if (status != null) tickets = tickets.filter { ticket -> ticket.status == status }

        notificationManager.reply(player, Messages.TITLES__YOUR_TICKETS)

        tickets.forEach {
            val replacements = Utils.ticketReplacements(it)

            notificationManager.reply(player, Messages.FORMAT__LIST_ITEM, *replacements)
        }
    }

    @Subcommand("%log")
    @CommandCompletion("@IssuerTicketIds")
    @CommandPermission(Constants.USER_PERMISSION + ".log")
    @Description("Log tickets messages")
    @Syntax("[Index]")
    fun onLog(player: Player, @Optional index: Int?) {
        val information = generateInformation(player, index, true)

        taskManager {
            val ticket = ticketManager[information.player, information.index] ?: return@taskManager
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(player, Messages.TITLES__TICKET_LOG, *replacements)

            ticket.messages.forEach {
                player.sendMessage("§f§l" + it.reason.name + " §8@ §f" + it.date?.formatted() + "§8 - §f" + (it.data
                        ?: it.sender.asName()))
            }
        }
    }
}