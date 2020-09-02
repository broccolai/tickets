package broccolai.tickets.commands;

import broccolai.corn.core.Lists;
import broccolai.tickets.events.TicketConstructionEvent;
import broccolai.tickets.exceptions.PureException;
import broccolai.tickets.locale.MessageNames;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.ticket.FutureTicket;
import broccolai.tickets.ticket.Message;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.utilities.Constants;
import broccolai.tickets.utilities.generic.ReplacementUtilities;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import java.util.List;
import org.bukkit.entity.Player;

/**
 * Command used for players to interact with their own tickets.
 */
@CommandAlias("ticket|ti")
@CommandPermission(Constants.USER_PERMISSION)
public class TicketCommand extends PureBaseCommand {
    /**
     * Creates a ticket with the players message.
     */
    @Subcommand("%create")
    @CommandPermission(Constants.USER_PERMISSION + ".create")
    @Description("Create a ticket")
    @Syntax("<Message>")
    public void onCreate(Player player, Message message) {
        taskManager.use()
            .async(() -> {
                TicketConstructionEvent constructionEvent = new TicketConstructionEvent(player, message);
                pluginManager.callEvent(constructionEvent);

                if (constructionEvent.hasException()) {
                    notificationManager.handleException(player, constructionEvent.getException());
                }
            })
            .execute();
    }

    /**
     * Update a ticket with the player supplied message.
     */
    @Subcommand("%update")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".update")
    @Description("Update a ticket")
    @Syntax("<Index> <Message>")
    public void onUpdate(Player player, @Flags("issuer") FutureTicket future, Message message) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) -> {
                try {
                    Ticket edited = ticketManager.update(ticket, message);
                    notificationManager.send(player, null, MessageNames.UPDATE_TICKET, edited);
                } catch (PureException e) {
                    notificationManager.basic(player, e.getMessageKey(), e.getReplacements());
                }
            })
            .execute();
    }

    /**
     * Close a players own ticket.
     */
    @Subcommand("%close")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".close")
    @Description("Close a ticket")
    @Syntax("[Index]")
    public void onClose(Player player, @Optional @AutoStatuses("OPEN,PICKED") @Flags("issuer") FutureTicket future) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) -> {
                try {
                    Ticket edited = ticketManager.close(player.getUniqueId(), ticket);
                    notificationManager.send(player, null, MessageNames.CLOSE_TICKET, edited);
                } catch (PureException e) {
                    notificationManager.basic(player, e.getMessageKey(), e.getReplacements());
                }
            })
            .execute();
    }

    /**
     * Displays information about a users own ticket.
     */
    @Subcommand("%show")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("[Index]")
    public void onShow(Player player, @Optional @Flags("issuer") FutureTicket future) {
        processShowCommand(getCurrentCommandIssuer(), future);
    }

    /**
     * Lists the players current open tickets.
     */
    @Subcommand("%list")
    @CommandCompletion("@TicketStatus")
    @CommandPermission(Constants.USER_PERMISSION + ".list")
    @Description("List all tickets")
    @Syntax("[Status]")
    public void onList(Player player, @Optional TicketStatus status) {
        taskManager.use()
            .async(() -> {
                List<Ticket> tickets = ticketSQL.selectAll(player.getUniqueId(), status);

                notificationManager.basic(player, Messages.TITLES__YOUR_TICKETS);

                tickets.forEach(ticket -> {
                    String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
                    notificationManager.basic(player, Messages.GENERAL__LIST_FORMAT, replacements);
                });
            })
            .execute();
    }

    /**
     * Displays a players ticket.
     */
    @Subcommand("%log")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".log")
    @Description("Log tickets messages")
    @Syntax("[Index]")
    public void onLog(Player player, @Optional @Flags("issuer") FutureTicket future) {
        processLogCommand(getCurrentCommandIssuer(), future);
    }
}
