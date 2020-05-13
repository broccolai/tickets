package co.uk.magmo.puretickets.commands

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Subcommand
import co.uk.magmo.puretickets.interactions.Notifications
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.ticket.Message
import co.uk.magmo.puretickets.ticket.TicketManager
import co.uk.magmo.puretickets.utils.Constants
import co.uk.magmo.puretickets.utils.asName
import co.uk.magmo.puretickets.utils.bold
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("ticket|ti")
class TicketCommand : PureBaseCommand() {
    @Default
    @HelpCommand
    fun onHelp(sender: CommandSender, help: CommandHelp) {
        help.showHelp()
    }

    @Subcommand("create|c")
    @CommandPermission(Constants.USER_PERMISSION + ".create")
    @Description("Create a ticket")
    fun onCreate(player: Player, message: Message) {
        val ticket = TicketManager.createTicket(player, message)
        Notifications.reply(player, Messages.TICKET__CREATED)
        Notifications.announce(Messages.NOTIFICATIONS__NEW_TICKET, "%picker%", player.name, "%id%", ticket.id.toString(), "%ticket%", ticket.currentMessage()!!)
    }

    @Subcommand("update|u")
    @CommandCompletion("@IssuerTicketIds")
    @CommandPermission(Constants.USER_PERMISSION + ".update")
    @Description("Update a ticket")
    fun onUpdate(player: Player, index: Int, message: Message) {
        val information = generateInformation(player, index)
        TicketManager.update(information, message)
        Notifications.reply(player, Messages.TICKET__UPDATED)
    }

    @Subcommand("close|cl")
    @CommandCompletion("@IssuerTicketIds")
    @CommandPermission(Constants.USER_PERMISSION + ".close")
    @Description("Close a ticket")
    fun onClose(player: Player, @Optional index: Int?) {
        val information = generateInformation(player, index)
        TicketManager.close(player, information)
        Notifications.reply(player, Messages.TICKET__CLOSED)
    }

    @Subcommand("show|s")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".show")
    @Description("Show a ticket")
    fun onShow(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index)

        TicketManager[information.player, information.index]?.apply {
            val picker = if (pickerUUID == null) "Unpicked" else pickerUUID.asName()

            Notifications.reply(sender, Messages.TITLES__SHOW_TICKET, "%id%", id.toString())

            sender.sendMessage("§bSender: §f" + playerUUID.asName())
            sender.sendMessage("§bPicker: §f" + picker)
            sender.sendMessage("§bDate Opened: §f" + dateOpened())
            sender.sendMessage("§bCurrent Message: §f" + currentMessage())
        }
    }

    @Subcommand("pick|p")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".pick")
    @Description("Pick a ticket")
    fun onPick(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index)
        TicketManager.pick(sender, information)
        Notifications.reply(sender, Messages.TICKET__PICKED)
        Notifications.send(information.player, Messages.NOTIFICATIONS__PICK, "%picker%", sender.name)
    }

    @Subcommand("done|d")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".done")
    @Description("Done-mark a ticket")
    fun onDone(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index)
        TicketManager.done(sender, information)
        Notifications.reply(sender, Messages.TICKET__DONE)
        Notifications.send(information.player, Messages.NOTIFICATIONS__DONE, "%picker%", sender.name)
    }

    @Subcommand("yield|y")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".yield")
    @Description("Yield a ticket")
    fun onYield(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index)
        TicketManager.yield(sender, information)
        Notifications.reply(sender, Messages.TICKET__YIELDED)
        Notifications.send(information.player, Messages.NOTIFICATIONS__YIELD, "%picker%", sender.name)
    }

    @Subcommand("reopen|ro")
    @CommandCompletion("@UserOfflineNames @UserOfflineTicketIDs")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reopen")
    @Description("Reopen a ticket")
    fun onReopen(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, true)
        TicketManager.reopen(sender, information)
        Notifications.reply(sender, Messages.TICKET__REOPENED)
        Notifications.send(information.player, Messages.NOTIFICATIONS__REOPEN, "%picker%", sender.name)
    }

    @Subcommand("log")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".log")
    @Description("Log tickets messages")
    fun onLog(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index)
        Notifications.reply(sender, Messages.TITLES__TICKET_LOG)
        TicketManager[information.player, information.index]?.messages?.forEach {
            sender.sendMessage(it.reason.name + " " + it.data)
        }
    }

    @Subcommand("list|l")
    @CommandPermission(Constants.STAFF_PERMISSION + ".list")
    @Description("List all tickets")
    fun onList(sender: CommandSender) {
        Notifications.reply(sender, Messages.TITLES__ALL_TICKETS)

        TicketManager.asMap().forEach { (uuid, tickets) ->
            sender.sendMessage(ChatColor.GREEN.toString() + uuid.asName())

            tickets.forEach { t -> sender.sendMessage(t.status.color.toString() + "#" + ChatColor.WHITE.bold() + t.id.toString() + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + t.currentMessage()) }
        }
    }
}