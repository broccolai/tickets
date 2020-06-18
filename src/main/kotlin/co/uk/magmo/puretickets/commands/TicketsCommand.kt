package co.uk.magmo.puretickets.commands

import co.aikar.commands.annotation.*
import co.uk.magmo.puretickets.locale.MessageNames
import co.uk.magmo.puretickets.locale.Messages
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
    @CommandCompletion("@TicketHolders @TargetIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("<Player> [Index]")
    fun onShow(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateId(offlinePlayer, index)

        processShowCommand(currentCommandIssuer, information)
    }

    @Subcommand("%pick")
    @CommandCompletion("@TicketHolders:status=OPEN @TargetIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".pick")
    @Description("Pick a ticket")
    @Syntax("<Player> [Index]")
    fun onPick(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val id = generateId(offlinePlayer, index, TicketStatus.OPEN, TicketStatus.PICKED)

        taskManager {
            val ticket = ticketManager.pick(sender.asUUID(), id) ?: return@taskManager

            notificationManager.send(sender, offlinePlayer.uniqueId, MessageNames.PICK_TICKET, ticket) { fields ->
                fields["PICKER"] = sender.name
            }
        }
    }

    @Subcommand("%assign")
    @CommandCompletion("@Players @TicketHolders:status=OPEN @TargetIds:parameter=2")
    @CommandPermission(Constants.STAFF_PERMISSION + ".assign")
    @Description("Assign a ticket to a staff member")
    @Syntax("<TargetPlayer> <Player> [Index]")
    fun onAssign(sender: CommandSender, target: OfflinePlayer, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val id = generateId(offlinePlayer, index, TicketStatus.OPEN, TicketStatus.PICKED)

        taskManager {
            val ticket = ticketManager.pick(target.uniqueId, id) ?: return@taskManager

            notificationManager.send(sender, target.uniqueId, MessageNames.ASSIGN_TICKET, ticket) { fields ->
                fields["ASSIGNER"] = sender.name
                fields["ASSIGNEE"] = target.name ?: ""
            }
        }
    }

    @Subcommand("%done")
    @CommandCompletion("@TicketHolders:status=PICK @TargetIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".done")
    @Description("Done-mark a ticket")
    @Syntax("<Player> [Index]")
    fun onDone(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val id = generateId(offlinePlayer, index, TicketStatus.OPEN, TicketStatus.PICKED)

        taskManager {
            val ticket = ticketManager.done(sender.asUUID(), id) ?: return@taskManager

            notificationManager.send(sender, offlinePlayer.uniqueId, MessageNames.DONE_TICKET, ticket)
        }
    }

    @Subcommand("%yield")
    @CommandCompletion("@TicketHolders:status=PICK @TargetIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".yield")
    @Description("Yield a ticket")
    @Syntax("<Player> [Index]")
    fun onYield(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val id = generateId(offlinePlayer, index, TicketStatus.PICKED)

        taskManager {
            val ticket = ticketManager.yield(sender.asUUID(), id) ?: return@taskManager

            notificationManager.send(sender, offlinePlayer.uniqueId, MessageNames.YIELD_TICKET, ticket)
        }
    }

    @Subcommand("%note")
    @CommandCompletion("@TicketHolders @TargetIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".note")
    @Description("Make a note on a ticket")
    @Syntax("<Player> <Index> <Message>")
    fun onNote(sender: CommandSender, offlinePlayer: OfflinePlayer, index: Int, message: String) {
        val id = generateId(offlinePlayer, index)

        taskManager {
            val ticket = ticketManager.note(sender.asUUID(), id, message) ?: return@taskManager

            notificationManager.send(sender, offlinePlayer.uniqueId, MessageNames.NOTE_TICKET, ticket) { fields ->
                fields["NOTE"] = message
            }
        }
    }

    @Subcommand("%reopen")
    @CommandCompletion("@TicketHolders:status=CLOSED @TargetIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reopen")
    @Description("Reopen a ticket")
    @Syntax("<Player> [Index]")
    fun onReopen(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val id = generateId(offlinePlayer, index, TicketStatus.CLOSED)

        taskManager {
            val ticket = ticketManager.reopen(sender.asUUID(), id) ?: return@taskManager

            notificationManager.send(sender, offlinePlayer.uniqueId, MessageNames.REOPEN_TICKET, ticket)
        }
    }

    @Subcommand("%teleport")
    @CommandCompletion("@TicketHolders @TargetIds:parameter=1")
    @CommandPermission(Constants.STAFF_PERMISSION + ".teleport")
    @Description("Teleport to a ticket creation location")
    @Syntax("<Player> [Index]")
    fun onTeleport(player: Player, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val id = generateId(offlinePlayer, index)

        taskManager {
            val ticket = ticketManager[id] ?: return@taskManager
            notificationManager.send(player, offlinePlayer.uniqueId, MessageNames.TELEPORT_TICKET, ticket)

            switchContext(SynchronizationContext.SYNC)
            player.teleport(ticket.location)
        }
    }

    @Subcommand("%log")
    @CommandCompletion("@TicketHolders @TargetIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".log")
    @Description("Log tickets messages")
    @Syntax("<Player> [Index]")
    fun onLog(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val id = generateId(offlinePlayer, index)

        processLogCommand(currentCommandIssuer, id)
    }

    @Subcommand("%list")
    @CommandCompletion("@TicketHolders @TicketStatus")
    @CommandPermission(Constants.STAFF_PERMISSION + ".list")
    @Description("List all tickets")
    @Syntax("[Player]")
    fun onList(sender: CommandSender, @Optional offlinePlayer: OfflinePlayer?, @Optional status: TicketStatus?) {
        val issuer = currentCommandIssuer

        taskManager {
            if (offlinePlayer != null) {
                var tickets = ticketManager[offlinePlayer.uniqueId]
                if (status != null) tickets = tickets.filter { ticket -> ticket.status == status }

                issuer.sendInfo(Messages.TITLES__SPECIFIC_TICKETS, "%player%", offlinePlayer.name!!)

                tickets.forEach { ticket ->
                    val replacements = Utils.ticketReplacements(ticket)

                    issuer.sendInfo(Messages.GENERAL__LIST_FORMAT, *replacements)
                }
            } else {
                issuer.sendInfo(Messages.TITLES__ALL_TICKETS)

                ticketManager.all().groupBy { it.playerUUID }.forEach { (uuid, tickets) ->
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
    @CommandCompletion("@TicketHolders")
    @CommandPermission(Constants.STAFF_PERMISSION + ".status")
    @Description("View amount of tickets in")
    @Syntax("[Player]")
    fun onStatus(sender: CommandSender, @Optional offlinePlayer: OfflinePlayer?) {
        val issuer = currentCommandIssuer

        taskManager {
            if (offlinePlayer != null) {
                issuer.sendInfo(Messages.TITLES__SPECIFIC_STATUS, "%player%", offlinePlayer.name!!)
            } else {
                issuer.sendInfo(Messages.TITLES__TICKET_STATUS)
            }

            val data = ticketManager.stats(offlinePlayer?.uniqueId)

            data.forEach { (status, amount) ->
                if (amount != 0) sender.sendMessage(amount.toString() + " " + status.name.toLowerCase())
            }
        }
    }
}