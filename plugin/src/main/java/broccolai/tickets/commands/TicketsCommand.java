package broccolai.tickets.commands;

import broccolai.corn.core.Lists;
import broccolai.tickets.exceptions.PureException;
import broccolai.tickets.locale.MessageNames;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.storage.TimeAmount;
import broccolai.tickets.ticket.FutureTicket;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.utilities.Constants;
import broccolai.tickets.utilities.generic.ReplacementUtilities;
import broccolai.tickets.utilities.generic.UserUtilities;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("tickets|tis")
@CommandPermission(Constants.STAFF_PERMISSION)
public class TicketsCommand extends PureBaseCommand {
    @Subcommand("%show")
    @CommandCompletion("@TicketHolders @TargetIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("<Player> [Index]")
    public void onShow(CommandSender sender, OfflinePlayer offlinePlayer, @Optional FutureTicket future) {
        processShowCommand(getCurrentCommandIssuer(), future);
    }

    @Subcommand("%pick")
    @CommandCompletion("@TicketHolders:status=OPEN @TargetIds:status=OPEN")
    @CommandPermission(Constants.STAFF_PERMISSION + ".pick")
    @Description("Pick a ticket")
    @Syntax("<Player> [Index]")
    public void onPick(CommandSender sender, OfflinePlayer offlinePlayer, @Optional @AutoStatuses("OPEN") FutureTicket future) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) -> {
                try {
                    Ticket edited = ticketManager.pick(UserUtilities.uuidFromSender(sender), ticket);

                    notificationManager.send(sender, offlinePlayer.getUniqueId(), MessageNames.PICK_TICKET, ticket);
                } catch (PureException e) {
                    notificationManager.basic(sender, e.getMessageKey(), e.getReplacements());
                }
            })
            .execute();
    }

    @Subcommand("%assign")
    @CommandCompletion("@Players @TicketHolders:status=OPEN @TargetIds:parameter=2,status=OPEN")
    @CommandPermission(Constants.STAFF_PERMISSION + ".assign")
    @Description("Assign a ticket to a staff member")
    @Syntax("<TargetPlayer> <Player> [Index]")
    public void onAssign(CommandSender sender, OfflinePlayer target, OfflinePlayer offlinePlayer, @Optional @AutoStatuses("OPEN") FutureTicket future) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) -> {
                try {
                    Ticket edited = ticketManager.pick(target.getUniqueId(), ticket);

                    notificationManager.send(sender, target.getUniqueId(), MessageNames.ASSIGN_TICKET, ticket);
                } catch (PureException e) {
                    notificationManager.basic(sender, e.getMessageKey(), e.getReplacements());
                }
            })
            .execute();
    }

    @Subcommand("%done")
    @CommandCompletion("@TicketHolders:status=PICK @TargetIds:status=PICK")
    @CommandPermission(Constants.STAFF_PERMISSION + ".done")
    @Description("Done-mark a ticket")
    @Syntax("<Player> [Index]")
    public void onDone(CommandSender sender, OfflinePlayer offlinePlayer, @Optional @AutoStatuses("PICKED") FutureTicket future) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) -> {
                try {
                    Ticket edited = ticketManager.done(UserUtilities.uuidFromSender(sender), ticket);

                    notificationManager.send(sender, offlinePlayer.getUniqueId(), MessageNames.DONE_TICKET, ticket);
                } catch (PureException e) {
                    notificationManager.basic(sender, e.getMessageKey(), e.getReplacements());
                }
            })
            .execute();
    }

    @Subcommand("%yield")
    @CommandCompletion("@TicketHolders:status=PICK @TargetIds:status=PICK")
    @CommandPermission(Constants.STAFF_PERMISSION + ".yield")
    @Description("Yield a ticket")
    @Syntax("<Player> [Index]")
    public void onYield(CommandSender sender, OfflinePlayer offlinePlayer, @Optional @AutoStatuses("PICKED") FutureTicket future) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) -> {
                try {
                    Ticket edited = ticketManager.yield(UserUtilities.uuidFromSender(sender), ticket);

                    notificationManager.send(sender, offlinePlayer.getUniqueId(), MessageNames.YIELD_TICKET, ticket);
                } catch (PureException e) {
                    notificationManager.basic(sender, e.getMessageKey(), e.getReplacements());
                }
            })
            .execute();
    }

    @Subcommand("%note")
    @CommandCompletion("@TicketHolders @TargetIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".note")
    @Description("Make a note on a ticket")
    @Syntax("<Player> <Index> <Message>")
    public void onNote(CommandSender sender, OfflinePlayer offlinePlayer, FutureTicket future, String message) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) -> {
                Ticket edited = ticketManager.note(UserUtilities.uuidFromSender(sender), ticket, message);

                notificationManager.send(sender, offlinePlayer.getUniqueId(), MessageNames.NOTE_TICKET, ticket);
            })
            .execute();
    }

    @Subcommand("%reopen")
    @CommandCompletion("@TicketHolders:status=CLOSED @TargetIds:status=CLOSED")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reopen")
    @Description("Reopen a ticket")
    @Syntax("<Player> [Index]")
    public void onReopen(CommandSender sender, OfflinePlayer offlinePlayer, @Optional @AutoStatuses("CLOSED") FutureTicket future) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) -> {
                try {
                    Ticket edited = ticketManager.reopen(UserUtilities.uuidFromSender(sender), ticket);

                    notificationManager.send(sender, offlinePlayer.getUniqueId(), MessageNames.REOPEN_TICKET, ticket);
                } catch (PureException e) {
                    notificationManager.basic(sender, e.getMessageKey(), e.getReplacements());
                }
            })
            .execute();
    }

    @Subcommand("%teleport")
    @CommandCompletion("@TicketHolders @TargetIds:parameter=1")
    @CommandPermission(Constants.STAFF_PERMISSION + ".teleport")
    @Description("Teleport to a ticket creation location")
    @Syntax("<Player> [Index]")
    public void onTeleport(Player sender, OfflinePlayer offlinePlayer, @Optional FutureTicket future) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) ->
                notificationManager.send(sender, offlinePlayer.getUniqueId(), MessageNames.TELEPORT_TICKET, ticket)
            )
            .future(future)
            .sync((ticket) -> sender.teleport(ticket.getLocation()))
            .execute();
    }

    @Subcommand("%log")
    @CommandCompletion("@TicketHolders @TargetIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".log")
    @Description("Log tickets messages")
    @Syntax("<Player> [Index]")
    public void onLog(CommandSender sender, OfflinePlayer offlinePlayer, @Optional FutureTicket future) {
        processLogCommand(getCurrentCommandIssuer(), future);
    }

    @Subcommand("%list")
    @CommandCompletion("@TicketHolders @TicketStatus")
    @CommandPermission(Constants.STAFF_PERMISSION + ".list")
    @Description("List all tickets")
    @Syntax("[Player]")
    public void onList(CommandSender sender, @Optional OfflinePlayer offlinePlayer, @Optional TicketStatus status) {
        taskManager.use()
            .async(() -> {
                if (offlinePlayer != null) {
                    List<Ticket> tickets = ticketManager.getAll(offlinePlayer.getUniqueId(), status);

                    notificationManager.basic(sender, Messages.TITLES__SPECIFIC_TICKETS, "%player%", offlinePlayer.getName());

                    tickets.forEach((ticket -> {
                        String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
                        notificationManager.basic(sender, Messages.GENERAL__LIST_FORMAT, replacements);
                    }));
                } else {
                    notificationManager.basic(sender, Messages.TITLES__ALL_TICKETS);

                    Lists.group(ticketManager.all(status), Ticket::getPlayerUUID).forEach((uuid, tickets) -> {
                        notificationManager.basic(sender, Messages.GENERAL__LIST_HEADER_FORMAT,
                            "%name%", UserUtilities.nameFromUUID(uuid));

                        tickets.forEach(ticket -> {
                            String[] replacements = ReplacementUtilities.ticketReplacements(ticket);
                            notificationManager.basic(sender, Messages.GENERAL__LIST_FORMAT, replacements);
                        });
                    });
                }
            })
            .execute();
    }

    @Subcommand("%status")
    @CommandCompletion("@TicketHolders")
    @CommandPermission(Constants.STAFF_PERMISSION + ".status")
    @Description("View amount of tickets in")
    @Syntax("[Player]")
    public void onStatus(CommandSender sender, @Optional OfflinePlayer offlinePlayer) {
        taskManager.use()
            .async(() -> {
                if (offlinePlayer != null) {
                    notificationManager.basic(sender, Messages.TITLES__SPECIFIC_STATUS, "%player%", offlinePlayer.getName());
                } else {
                    notificationManager.basic(sender, Messages.TITLES__TICKET_STATUS);
                }

                EnumMap<TicketStatus, Integer> data = ticketManager.stats(offlinePlayer != null ? offlinePlayer.getUniqueId() : null);

                data.forEach((status, amount) -> {
                    if (amount != 0) {
                        sender.sendMessage(amount.toString() + " " + status.name().toLowerCase());
                    }
                });
            })
            .execute();
    }

    @Subcommand("%highscore")
    @CommandCompletion("@TimeAmounts")
    @CommandPermission(Constants.STAFF_PERMISSION + ".highscore")
    @Description("View highscores of ticket completions")
    public void onHighscore(CommandSender sender, TimeAmount amount) {
        taskManager.use()
            .async(() -> {
                Map<UUID, Integer> highscores = ticketManager.highscores(amount);
                notificationManager.basic(sender, Messages.TITLES__HIGHSCORES);

                highscores.forEach((uuid, number) ->
                    notificationManager.basic(sender, Messages.GENERAL__HS_FORMAT,
                        "%target%", UserUtilities.nameFromUUID(uuid), "%amount%", number.toString())
                );
            })
            .execute();
    }
}
