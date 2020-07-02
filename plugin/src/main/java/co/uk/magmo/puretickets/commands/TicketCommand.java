package co.uk.magmo.puretickets.commands;

import co.aikar.commands.annotation.*;
import co.uk.magmo.puretickets.ticket.FutureTicket;
import co.uk.magmo.puretickets.ticket.Message;
import co.uk.magmo.puretickets.utilities.Constants;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("ticket|ti")
@CommandPermission(Constants.USER_PERMISSION)
public class TicketCommand extends PureBaseCommand {
    @Subcommand("%create")
    @CommandPermission(Constants.USER_PERMISSION + ".create")
    @Description("Create a ticket")
    @Syntax("<Message>")
    public void onCreate(Player player, Message message) {
            if (ticketManager.count(player.getUniqueId()) >= config.LIMIT_OPEN_TICKET) {
//                throw new TooManyOpenTickets();
            }

//            val ticket = ticketManager.createTicket(player, message)
//
//            notificationManager.send(player, null, MessageNames.NEW_TICKET, ticket) { fields ->
//                    fields["MESSAGE"] = message.data!!
//            }
    }

//    @Subcommand("%update")
//    @CommandCompletion("@IssuerIds")
//    @CommandPermission(Constants.USER_PERMISSION + ".update")
//    @Description("Update a ticket")
//    @Syntax("<Index> <Message>")
//    fun onUpdate(player: Player, index: Int, message: Message) {
//        val id = generateId(player, index, TicketStatus.OPEN, TicketStatus.PICKED)
//
//        taskManager {
//            val ticket = ticketManager.update(id, message) ?: return@taskManager
//
//                    notificationManager.send(player, null, MessageNames.UPDATE_TICKET, ticket) { fields ->
//                    fields["MESSAGE"] = message.data!!
//            }
//        }
//    }
//
//    @Subcommand("%close")
//    @CommandCompletion("@IssuerIds")
//    @CommandPermission(Constants.USER_PERMISSION + ".close")
//    @Description("Close a ticket")
//    @Syntax("[Index]")
//    public void onClose(Player player, @Optional @Flags("issuer") FutureTicket future) {
//        val id = generateId(player, index, TicketStatus.OPEN, TicketStatus.PICKED)
//
//        taskManager {
//            val ticket = ticketManager.close(player.asUUID(), id) ?: return@taskManager
//
//                    notificationManager.send(player, null, MessageNames.CLOSE_TICKET, ticket)
//        }
//    }

    @Subcommand("%show")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("[Index]")
    public void onShow(Player player, @Optional @Flags("issuer") FutureTicket future) {
        processShowCommand(getCurrentCommandIssuer(), future);
    }
}
