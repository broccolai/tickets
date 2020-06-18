package co.uk.magmo.puretickets.commands

import co.aikar.commands.annotation.*
import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.exceptions.TooManyOpenTickets
import co.uk.magmo.puretickets.locale.MessageNames
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.ticket.*
import co.uk.magmo.puretickets.utils.*
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
            if (ticketManager[player.uniqueId].size >= Config.limitOpenTicket) {
                throw TooManyOpenTickets()
            }

            val ticket = ticketManager.createTicket(player, message)

            notificationManager.send(player, null, MessageNames.NEW_TICKET, ticket) { fields ->
                fields["MESSAGE"] = message.data!!
            }
        }
    }

    @Subcommand("%update")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".update")
    @Description("Update a ticket")
    @Syntax("<Index> <Message>")
    fun onUpdate(player: Player, index: Int, message: Message) {
        val id = generateId(player, index, TicketStatus.OPEN, TicketStatus.PICKED)

        taskManager {
            val ticket = ticketManager.update(id, message) ?: return@taskManager

            notificationManager.send(player, null, MessageNames.UPDATE_TICKET, ticket) { fields ->
                fields["MESSAGE"] = message.data!!
            }
        }
    }

    @Subcommand("%close")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".close")
    @Description("Close a ticket")
    @Syntax("[Index]")
    fun onClose(player: Player, @Optional index: Int?) {
        val id = generateId(player, index, TicketStatus.OPEN, TicketStatus.PICKED)

        taskManager {
            val ticket = ticketManager.close(player.asUUID(), id) ?: return@taskManager

            notificationManager.send(player, null, MessageNames.CLOSE_TICKET, ticket)
        }
    }

    @Subcommand("%show")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("[Index]")
    fun onShow(player: Player, @Optional index: Int?) {
        val id = generateId(player, index)

        processShowCommand(currentCommandIssuer, id)
    }

    @Subcommand("%list")
    @CommandCompletion("@TicketStatus")
    @CommandPermission(Constants.USER_PERMISSION + ".list")
    @Description("List all tickets")
    @Syntax("[Status]")
    fun onList(player: Player, @Optional status: TicketStatus?) {
        var tickets = ticketManager[player.uniqueId]

        if (status != null) tickets = tickets.filter { ticket -> ticket.status == status }

        currentCommandIssuer.sendInfo(Messages.TITLES__YOUR_TICKETS)

        tickets.forEach {
            val replacements = Utils.ticketReplacements(it)

            currentCommandIssuer.sendInfo(Messages.GENERAL__LIST_FORMAT, *replacements)
        }
    }

    @Subcommand("%log")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".log")
    @Description("Log tickets messages")
    @Syntax("[Index]")
    fun onLog(player: Player, @Optional index: Int?) {
        val id = generateId(player, index)

        processLogCommand(currentCommandIssuer, id)
    }
}