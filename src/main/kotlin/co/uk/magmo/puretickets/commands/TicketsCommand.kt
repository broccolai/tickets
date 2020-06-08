package co.uk.magmo.puretickets.commands

import co.aikar.commands.annotation.*
import co.uk.magmo.puretickets.locale.MessageNames
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.ticket.TicketStatus
import co.uk.magmo.puretickets.utils.*
import com.okkero.skedule.SynchronizationContext
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("tickets|tis")
@CommandPermission(Constants.STAFF_PERMISSION)
class TicketsCommand : PureBaseCommand() {
    @Subcommand("%show")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("<Player> [Index]")
    fun onShow(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        processShowCommand(currentCommandIssuer, information)
    }

    @Subcommand("%pick")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".pick")
    @Description("Pick a ticket")
    @Syntax("<Player> [Index]")
    fun onPick(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager.pick(sender.asUUID(), information)

            notificationManager.send(sender, offlinePlayer.uniqueId, MessageNames.PICK_TICKET, ticket) { fields ->
                fields["PICKER"] = sender.name
            }
        }
    }

    @Subcommand("%assign")
    @CommandCompletion("@Players @AllTicketHolders @UserTicketIdsWithTarget")
    @CommandPermission(Constants.STAFF_PERMISSION + ".assign")
    @Description("Assign a ticket to a staff member")
    @Syntax("<TargetPlayer> <Player> [Index]")
    fun onAssign(sender: CommandSender, target: OfflinePlayer, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager.pick(target.uniqueId, information)

            notificationManager.send(sender, target.uniqueId, MessageNames.ASSIGN_TICKET, ticket) { fields ->
                fields["ASSIGNER"] = sender.name
                fields["ASSIGNEE"] = target.name ?: ""
            }
        }
    }

    @Subcommand("%done")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".done")
    @Description("Done-mark a ticket")
    @Syntax("<Player> [Index]")
    fun onDone(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager.done(sender.asUUID(), information)

            notificationManager.send(sender, offlinePlayer.uniqueId, MessageNames.DONE_TICKET, ticket)
        }
    }

    @Subcommand("%yield")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".yield")
    @Description("Yield a ticket")
    @Syntax("<Player> [Index]")
    fun onYield(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager.yield(sender.asUUID(), information)

            notificationManager.send(sender, offlinePlayer.uniqueId, MessageNames.YIELD_TICKET, ticket)
        }
    }

    @Subcommand("%note")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".note")
    @Description("Make a note on a ticket")
    @Syntax("<Player> <Index> <Message>")
    fun onNote(sender: CommandSender, offlinePlayer: OfflinePlayer, index: Int, message: String) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager.note(sender.asUUID(), information, message)

            notificationManager.send(sender, offlinePlayer.uniqueId, MessageNames.NOTE_TICKET, ticket) { fields ->
                fields["NOTE"] = message
            }
        }
    }

    @Subcommand("%reopen")
    @CommandCompletion("@UserOfflineNames @UserOfflineTicketIDs")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reopen")
    @Description("Reopen a ticket")
    @Syntax("<Player> [Index]")
    fun onReopen(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, true)

        taskManager {
            val ticket = ticketManager.reopen(sender.asUUID(), information)

            notificationManager.send(sender, offlinePlayer.uniqueId, MessageNames.REOPEN_TICKET, ticket)
        }
    }

    @Subcommand("%teleport")
    @CommandCompletion("@AllTicketHolders @UserTicketIdsWithPlayer")
    @CommandPermission(Constants.STAFF_PERMISSION + ".teleport")
    @Description("Teleport to a ticket creation location")
    @Syntax("<Player> [Index]")
    fun onTeleport(player: Player, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager[offlinePlayer.uniqueId, information.index]!!
            notificationManager.send(player, offlinePlayer.uniqueId, MessageNames.TELEPORT_TICKET, ticket)

            switchContext(SynchronizationContext.SYNC)
            player.teleport(ticket.location)
        }
    }

    @Subcommand("%log")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".log")
    @Description("Log tickets messages")
    @Syntax("<Player> [Index]")
    fun onLog(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, true)

        processLogCommand(currentCommandIssuer, information)
    }

    @Subcommand("%list")
    @CommandCompletion("@UserNames @TicketStatus")
    @CommandPermission(Constants.STAFF_PERMISSION + ".list")
    @Description("List all tickets")
    @Syntax("[Player]")
    fun onList(sender: CommandSender, @Optional offlinePlayer: OfflinePlayer?, @Optional status: TicketStatus?) {
        val issuer = currentCommandIssuer

        taskManager {
            if (offlinePlayer != null) {
                var tickets = SQLFunctions.retrieveClosedTickets(offlinePlayer.uniqueId)
                if (status != null) tickets = tickets.filter { ticket -> ticket.status == status }

                issuer.sendInfo(Messages.TITLES__SPECIFIC_TICKETS, "%player%", offlinePlayer.name!!)

                tickets.forEach { ticket ->
                    val replacements = Utils.ticketReplacements(ticket)

                    issuer.sendInfo(Messages.GENERAL__LIST_FORMAT, *replacements)
                }
            } else {
                issuer.sendInfo(Messages.TITLES__ALL_TICKETS)

                ticketManager.asMap().forEach { (uuid, tickets) ->
                    sender.sendMessage(ChatColor.GREEN.toString() + uuid.asName())

                    tickets.forEach { ticket ->
                        val replacements = Utils.ticketReplacements(ticket)

                        issuer.sendInfo(Messages.GENERAL__LIST_FORMAT, *replacements)
                    }
                }
            }
        }
    }

    @Subcommand("%status")
    @CommandCompletion("@UserNames")
    @CommandPermission(Constants.STAFF_PERMISSION + ".status")
    @Description("View amount of tickets in")
    @Syntax("[Player]")
    fun onStatus(sender: CommandSender, @Optional offlinePlayer: OfflinePlayer?) {
        val issuer = currentCommandIssuer

        taskManager {
            val data = if (offlinePlayer != null) {
                issuer.sendInfo(Messages.TITLES__SPECIFIC_STATUS, "%player%", offlinePlayer.name!!)

                SQLFunctions.selectCurrentTickets(offlinePlayer.uniqueId)
            } else {
                issuer.sendInfo(Messages.TITLES__TICKET_STATUS)

                SQLFunctions.selectCurrentTickets(null)
            }

            data.forEach { (status, amount) ->
                if (amount != 0) sender.sendMessage(amount.toString() + " " + status.name.toLowerCase())
            }
        }
    }
}