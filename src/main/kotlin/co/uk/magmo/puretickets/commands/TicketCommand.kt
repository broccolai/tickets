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
        TicketManager.createTicket(player, message)
        Notifications.reply(player, Messages.TICKET__CREATED)
    }

    @Subcommand("close|cl")
    @CommandCompletion("@UsersTickets")
    @CommandPermission(Constants.USER_PERMISSION + ".close")
    @Description("Close a ticket")
    fun onClose(player: Player, @Optional index: Int) {
        val information = generateInformation(player, index)
        TicketManager.close(player, information)
        Notifications.reply(player, Messages.TICKET__CLOSED)
    }

    @Subcommand("pick|p")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".pick")
    @Description("Pick a ticket")
    fun onPick(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index)
        TicketManager.pick(sender, information)
        Notifications.reply(sender, Messages.TICKET__PICKED)
        Notifications.send(information.player, Messages.TICKET__CREATED, "%player%", sender.name);
    }

    @Subcommand("done|d")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".done")
    @Description("Done-mark a ticket")
    fun onDone(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int) {
        val information = generateInformation(offlinePlayer, index)
        TicketManager.done(sender, information)
        Notifications.reply(sender, Messages.TICKET__DONE)
    }

    @Subcommand("show|s")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".show")
    @Description("Show a ticket")
    fun onShow(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int) {
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

    @Subcommand("reopen|ro")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reopen")
    @Description("Reopen a ticket")
    fun onReopen(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int) {
        val information = generateInformation(offlinePlayer, index)
    }

    @Subcommand("list|l")
    @CommandPermission(Constants.STAFF_PERMISSION + ".list")
    @Description("List all tickets")
    fun onList(sender: CommandSender) {
        Notifications.reply(sender, Messages.TITLES__ALL_TICKETS)

        TicketManager.asMap().forEach { (uuid, tickets) ->
            sender.sendMessage(ChatColor.AQUA.toString() + uuid.asName()!!)

            tickets.forEach { t -> sender.sendMessage(t.status.color.toString() + "#" + ChatColor.WHITE.bold() + t.id.toString() + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + t.currentMessage()) }
        }
    }
}