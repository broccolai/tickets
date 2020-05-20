package co.uk.magmo.puretickets.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.annotation.*
import co.uk.magmo.puretickets.interactions.NotificationManager
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.ticket.*
import co.uk.magmo.puretickets.utils.*
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("ticket|ti")
class TicketCommand : BaseCommand() {
    @Dependency
    private lateinit var notificationManager: NotificationManager
    @Dependency
    private lateinit var ticketManager: TicketManager
    
    @Default
    @HelpCommand
    fun onHelp(sender: CommandSender, help: CommandHelp) {
        help.helpEntries.removeAt(0)
        help.showHelp()
    }

    @Subcommand("%create")
    @CommandPermission(Constants.USER_PERMISSION + ".create")
    @Description("Create a ticket")
    @Syntax("<Message>")
    fun onCreate(player: Player, message: Message) {
        val ticket = ticketManager.createTicket(player, message)
        val replacements = Utils.ticketReplacements(ticket)

        notificationManager.reply(player, Messages.TICKET__CREATED, *replacements)
        notificationManager.announce(Messages.ANNOUNCEMENTS__NEW_TICKET, "%user%", player.name, *replacements)
    }

    @Subcommand("%update")
    @CommandCompletion("@IssuerTicketIds")
    @CommandPermission(Constants.USER_PERMISSION + ".update")
    @Description("Update a ticket")
    @Syntax("<Index> <Message>")
    fun onUpdate(player: Player, index: Int, message: Message) {
        val information = generateInformation(player, index, false)
        val ticket = ticketManager.update(information, message)
        val replacements = Utils.ticketReplacements(ticket)

        notificationManager.reply(player, Messages.TICKET__UPDATED, *replacements)
        notificationManager.announce(Messages.ANNOUNCEMENTS__UPDATED_TICKET, "%user%", player.name, *replacements)
    }

    @Subcommand("%close")
    @CommandCompletion("@IssuerTicketIds")
    @CommandPermission(Constants.USER_PERMISSION + ".close")
    @Description("Close a ticket")
    @Syntax("[Index]")
    fun onClose(player: Player, @Optional index: Int?) {
        val information = generateInformation(player, index, false)
        val ticket = ticketManager.close(player.asUUID(), information)
        val replacements = Utils.ticketReplacements(ticket)

        notificationManager.reply(player, Messages.TICKET__CLOSED, *replacements)
        notificationManager.announce(Messages.ANNOUNCEMENTS__CLOSED_TICKET, "%user%", player.name, *replacements)
    }

    @Subcommand("%show")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("<Player> [Index]")
    fun onShow(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)
        val ticket = ticketManager[information.player, information.index] ?: return
        val replacements = Utils.ticketReplacements(ticket)

        val message = ticket.currentMessage()!!

        notificationManager.reply(sender, Messages.TITLES__SHOW_TICKET, *replacements)
        notificationManager.reply(sender, Messages.SHOW__SENDER, "%player%", ticket.playerUUID.asName(), "%date%", ticket.dateOpened().formatted())
        notificationManager.reply(sender, Messages.SHOW__MESSAGE, "%message%", message.data!!, "%date%", message.date.formatted())

        if (ticket.status != TicketStatus.PICKED) {
            notificationManager.reply(sender, Messages.SHOW__UNPICKED)
        } else {
            notificationManager.reply(sender, Messages.SHOW__PICKER, "%player%", ticket.pickerUUID.asName(),
                    "%date%", ticket.messages.last { it.reason == MessageReason.PICKED }.date.formatted())
        }
    }

    @Subcommand("%pick")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".pick")
    @Description("Pick a ticket")
    @Syntax("<Player> [Index]")
    fun onPick(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)
        val ticket = ticketManager.pick(sender.asUUID(), information)
        val replacements = Utils.ticketReplacements(ticket)

        notificationManager.reply(sender, Messages.TICKET__PICKED, *replacements)
        notificationManager.send(information.player, Messages.NOTIFICATIONS__PICK, "%user%", sender.name, *replacements)
        notificationManager.announce(Messages.ANNOUNCEMENTS__PICKED_TICKET, "%user%", sender.name, *replacements)
    }

    @Subcommand("%assign")
    @CommandCompletion("@Players @AllTicketHolders @UserTicketIdsWithTarget")
    @CommandPermission(Constants.STAFF_PERMISSION + ".assign")
    @Description("Assign a ticket to a staff member")
    @Syntax("<TargetPlayer> <Player> [Index]")
    fun onAssign(sender: CommandSender, target: OfflinePlayer, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)
        val ticket = ticketManager.pick(target.uniqueId, information)
        val replacements = Utils.ticketReplacements(ticket)

        notificationManager.reply(sender, Messages.TICKET__ASSIGN, "%target%", target.name!! , *replacements)
        notificationManager.send(target.uniqueId, Messages.NOTIFICATIONS__ASSIGN, "%user%", sender.name, *replacements)
        notificationManager.announce(Messages.ANNOUNCEMENTS__ASSIGN_TICKET, "%user%", sender.name, "%target%", target.name!!, *replacements)
    }

    @Subcommand("%done")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".done")
    @Description("Done-mark a ticket")
    @Syntax("<Player> [Index]")
    fun onDone(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)
        val ticket = ticketManager.done(sender.asUUID(), information)
        val replacements = Utils.ticketReplacements(ticket)

        notificationManager.reply(sender, Messages.TICKET__DONE, *replacements)
        notificationManager.send(information.player, Messages.NOTIFICATIONS__DONE, "%user%", sender.name, *replacements)
        notificationManager.announce(Messages.ANNOUNCEMENTS__DONE_TICKET, "%user%", sender.name, *replacements)
    }

    @Subcommand("%yield")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".yield")
    @Description("Yield a ticket")
    @Syntax("<Player> [Index]")
    fun onYield(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)
        val ticket = ticketManager.yield(sender.asUUID(), information)
        val replacements = Utils.ticketReplacements(ticket)

        notificationManager.reply(sender, Messages.TICKET__YIELDED, *replacements)
        notificationManager.send(information.player, Messages.NOTIFICATIONS__YIELD, "%user%", sender.name, *replacements)
        notificationManager.announce(Messages.ANNOUNCEMENTS__YIELDED_TICKET, "%user%", sender.name, *replacements)
    }

    @Subcommand("%reopen")
    @CommandCompletion("@UserOfflineNames @UserOfflineTicketIDs")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reopen")
    @Description("Reopen a ticket")
    @Syntax("<Player> [Index]")
    fun onReopen(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, true)
        val ticket = ticketManager.reopen(sender.asUUID(), information)
        val replacements = Utils.ticketReplacements(ticket)

        notificationManager.reply(sender, Messages.TICKET__REOPENED, *replacements)
        notificationManager.send(information.player, Messages.NOTIFICATIONS__REOPEN, "%user%", sender.name, *replacements)
        notificationManager.announce(Messages.ANNOUNCEMENTS__REOPEN_TICKET, "%user%", sender.name, *replacements)
    }

    @Subcommand("%teleport")
    @CommandCompletion("@AllTicketHolders @UserTicketIdsWithPlayer")
    @CommandPermission(Constants.STAFF_PERMISSION + ".teleport")
    @Description("Teleport to a ticket creation location")
    @Syntax("<Player> [Index]")
    fun onTeleport(player: Player, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)
        val ticket = ticketManager[offlinePlayer.uniqueId, information.index]
        val replacements = Utils.ticketReplacements(ticket)
        val location = ticket?.location

        if (location == null) {
            notificationManager.reply(player, Messages.TICKET__TELEPORT_ERROR, *replacements)
        } else {
            notificationManager.reply(player, Messages.TICKET__TELEPORT, *replacements)
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
        val ticket = ticketManager[information.player, information.index] ?: return
        val replacements = Utils.ticketReplacements(ticket)

        notificationManager.reply(sender, Messages.TITLES__TICKET_LOG, *replacements)

        ticket.messages.forEach {
            sender.sendMessage("§f§l" + it.reason.name + " §8@ §f" + it.date?.formatted() + "§8 - §f" + (it.data
                    ?: it.sender.asName()))
        }
    }

    @Subcommand("%list")
    @CommandCompletion("@UserNames @TicketStatus")
    @CommandPermission(Constants.STAFF_PERMISSION + ".list")
    @Description("List all tickets")
    @Syntax("[Player]")
    fun onList(sender: CommandSender, @Optional offlinePlayer: OfflinePlayer?, @Optional status: TicketStatus?) {
        if (offlinePlayer != null) {
            notificationManager.reply(sender, Messages.TITLES__SPECIFIC_TICKETS, "%player%", offlinePlayer.name!!)

            var tickets = SQLFunctions.retrieveClosedTickets(offlinePlayer.uniqueId)

            if (status != null) tickets = tickets.filter { ticket -> ticket.status == status }

            tickets.forEach { ticket ->
                val replacements = Utils.ticketReplacements(ticket)

                notificationManager.reply(sender, Messages.FORMAT__LIST_ITEM, *replacements)
            }
        } else {
            notificationManager.reply(sender, Messages.TITLES__ALL_TICKETS)

            ticketManager.asMap().forEach { (uuid, tickets) ->
                sender.sendMessage(ChatColor.GREEN.toString() + uuid.asName())

                tickets.forEach { ticket ->
                    val replacements = Utils.ticketReplacements(ticket)

                    notificationManager.reply(sender, Messages.FORMAT__LIST_ITEM, *replacements)
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
        val data = if (offlinePlayer != null) {
            notificationManager.reply(sender, Messages.TITLES__SPECIFIC_STATUS, "%player%", offlinePlayer.name!!)
            SQLFunctions.selectCurrentTickets(offlinePlayer.uniqueId)
        } else {
            notificationManager.reply(sender, Messages.TITLES__TICKET_STATUS)
            SQLFunctions.selectCurrentTickets(null)
        }

        data.forEach { (status, amount) ->
            if (amount != 0) sender.sendMessage(amount.toString() + " " + status.name.toLowerCase())
        }
    }

    private fun generateInformation(offlinePlayer: OfflinePlayer, input: Int?, offline: Boolean): TicketInformation {
        var index = input

        if (index == null) {
            index = SQLFunctions.highestTicket(offlinePlayer.uniqueId, offline) ?: throw InvalidCommandArgument()
        } else {
            if (!ticketManager[offlinePlayer.uniqueId].any { it.id == input }) {
                if (offline) {
                    if (!SQLFunctions.ticketExists(offlinePlayer.uniqueId, index)) throw InvalidCommandArgument()
                } else {
                    throw InvalidCommandArgument()
                }
            }
        }

        return TicketInformation(offlinePlayer.uniqueId, index)
    }
}