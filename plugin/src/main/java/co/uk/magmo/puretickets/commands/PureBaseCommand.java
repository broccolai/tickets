package co.uk.magmo.puretickets.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.uk.magmo.puretickets.interactions.NotificationManager;
import co.uk.magmo.puretickets.tasks.TaskManager;
import co.uk.magmo.puretickets.ticket.Ticket;
import co.uk.magmo.puretickets.ticket.TicketManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@CommandAlias("test")
public class PureBaseCommand extends BaseCommand {

    @Dependency
    protected NotificationManager notificationManager;

    @Dependency
    protected TicketManager ticketManager;

    @Dependency
    protected TaskManager taskManager;

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.getHelpEntries().remove(0);
        help.showHelp();
    }

    @Subcommand("test")
    @CommandCompletion("@TicketHolders @TargetIds")
    public void onTest(CommandSender sender, OfflinePlayer player, Ticket ticket) {
        sender.sendMessage(ticket.getId().toString());
//        System.out.println(t1);
//        System.out.println(t2);
    }

//    protected void processShowCommand(CommandIssuer issuer, Integer id) {
//        taskManager.use().async(() -> {
//
//        });
//        taskManager {
//            val ticket = ticketManager[id] ?:return @taskManager
//                    val replacements = Utils.ticketReplacements(ticket)
//            val message = ticket.currentMessage() !!
//
//                    issuer.sendInfo(Messages.TITLES__SHOW_TICKET, * replacements)
//            issuer.sendInfo(Messages.SHOW__SENDER, "%player%", ticket.playerUUID.asName(), "%date%", ticket.dateOpened().formatted())
//            issuer.sendInfo(Messages.SHOW__MESSAGE, "%message%", message.data !!, "%date%", message.date.formatted())
//
//            if (ticket.status != TicketStatus.PICKED) {
//                issuer.sendInfo(Messages.SHOW__UNPICKED)
//            } else {
//                issuer.sendInfo(Messages.SHOW__PICKER, "%player%", ticket.pickerUUID.asName(),
//                        "%date%", ticket.messages.last {
//                    it.reason == MessageReason.PICKED
//                }.date.formatted())
//            }
//        }
//    }
//
//    protected fun processLogCommand(issuer:CommandIssuer, id:Int) {
//        taskManager {
//            val ticket = ticketManager[id] ?:return @taskManager
//                    val replacements = Utils.ticketReplacements(ticket)
//
//            issuer.sendInfo(Messages.TITLES__TICKET_LOG, * replacements)
//
//            ticket.messages.forEach {
//                val suffix = it.data ?:it.sender.asName()
//                issuer.sendInfo(Messages.GENERAL__LOG_FORMAT, "%reason%", it.reason.name, "%date%", it.date.formatted(), "%suffix%", suffix)
//            }
//        }
//    }
//
//
//    protected fun generateId(player:OfflinePlayer, input:Int?, vararg status:TicketStatus):
//
//    Int {
//        var index = input
//
//        if (index == null) {
//            index = ticketManager.getHighest(player.uniqueId, * status)
//        ?:ticketManager.getHighest(player.uniqueId, * status) ?:throw TicketNotFound()
//        } else if (!ticketManager.exists(index)) {
//            throw TicketNotFound()
//        }
//
//        return index
//    }
}
