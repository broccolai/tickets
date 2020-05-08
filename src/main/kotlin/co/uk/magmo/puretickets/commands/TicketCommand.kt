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
        Notifications.reply(player, Messages.TICKET_CREATED)
    }

    @Subcommand("close|cl")
    @CommandCompletion("@UsersTickets")
    @CommandPermission(Constants.USER_PERMISSION + ".close")
    @Description("Close a ticket")
    fun onClose(player: Player, @Optional index: Int) {
        val information = generateInformation(player, index)
        TicketManager.close(player, information)
        Notifications.reply(player, Messages.TICKET_CLOSED)
    }

    @Subcommand("pick|p")
    @CommandCompletion("@TicketHolders @HolderTickets")
    @CommandPermission(Constants.STAFF_PERMISSION + ".pick")
    @Description("Pick a ticket")
    fun onPick(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index)
        TicketManager.pick(sender, information)
        Notifications.reply(sender, Messages.TICKET_PICKED)
        Notifications.send(information.player, Messages.TICKET_CREATED, "%player%", sender.name);
    }

    @Subcommand("done|d")
    @CommandPermission(Constants.STAFF_PERMISSION + ".done")
    @Description("Done-mark a ticket")
    fun onDone(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int) {
        val information = generateInformation(offlinePlayer, index)
        TicketManager.done(sender, information)
        Notifications.reply(sender, Messages.TICKET_DONE)
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
        TicketManager.all().forEach { t -> sender.sendMessage(t.playerUUID.asName() + " -- " + t.currentMessage() + " -- " + t.status.name) }
    }
}