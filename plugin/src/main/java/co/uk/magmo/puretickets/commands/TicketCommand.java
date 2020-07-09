package co.uk.magmo.puretickets.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.uk.magmo.corn.core.Lists;
import co.uk.magmo.puretickets.exceptions.PureException;
import co.uk.magmo.puretickets.locale.MessageNames;
import co.uk.magmo.puretickets.locale.Messages;
import co.uk.magmo.puretickets.ticket.FutureTicket;
import co.uk.magmo.puretickets.ticket.Message;
import co.uk.magmo.puretickets.ticket.Ticket;
import co.uk.magmo.puretickets.ticket.TicketStatus;
import co.uk.magmo.puretickets.utilities.Constants;
import co.uk.magmo.puretickets.utilities.generic.ReplacementUtilities;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
@CommandAlias("ticket|ti")
@CommandPermission(Constants.USER_PERMISSION)
public class TicketCommand extends PureBaseCommand {
    @Subcommand("%create")
    @CommandPermission(Constants.USER_PERMISSION + ".create")
    @Description("Create a ticket")
    @Syntax("<Message>")
    public void onCreate(Player player, Message message) {
        taskManager.use()
                .async(() -> {
                    try {
                        Ticket ticket = ticketManager.createTicket(player, message);
                        notificationManager.send(player, null, MessageNames.NEW_TICKET, ticket,
                                fields -> fields.put("MESSAGE", message.getData()));
                    } catch (PureException e) {
                        notificationManager.basic(player, e.getMessageKey(), e.getReplacements());
                    }
                })
                .execute();
    }

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
                        notificationManager.send(player, null, MessageNames.UPDATE_TICKET, edited,
                                fields -> fields.put("MESSAGE", message.getData()));
                    } catch (PureException e) {
                        notificationManager.basic(player, e.getMessageKey(), e.getReplacements());
                    }
                })
                .execute();
    }

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
                        notificationManager.send(player, null, MessageNames.CLOSE_TICKET, edited, null);
                    } catch (PureException e) {
                        notificationManager.basic(player, e.getMessageKey(), e.getReplacements());
                    }
                })
                .execute();
    }

    @Subcommand("%show")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("[Index]")
    public void onShow(Player player, @Optional @Flags("issuer") FutureTicket future) {
        processShowCommand(getCurrentCommandIssuer(), future);
    }

    @Subcommand("%list")
    @CommandCompletion("@TicketStatus")
    @CommandPermission(Constants.USER_PERMISSION + ".list")
    @Description("List all tickets")
    @Syntax("[Status]")
    public void onList(Player player, @Optional TicketStatus status) {
        taskManager.use()
                .async(() -> {
                    List<Ticket> tickets = ticketManager.getAll(player.getUniqueId(), null);

                    tickets = Lists.filter(tickets, ticket ->
                            status == null ? ticket.getStatus() != TicketStatus.CLOSED : ticket.getStatus() == status
                    );

                    notificationManager.basic(player, Messages.TITLES__YOUR_TICKETS);

                    tickets.forEach(ticket -> {
                        String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
                        notificationManager.basic(player, Messages.GENERAL__LIST_FORMAT, replacements);
                    });
                })
                .execute();
    }

    @Subcommand("%log")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".log")
    @Description("Log tickets messages")
    @Syntax("[Index]")
    public void onLog(Player player, @Optional @Flags("issuer") FutureTicket future) {
        processLogCommand(getCurrentCommandIssuer(), future);
    }
}
