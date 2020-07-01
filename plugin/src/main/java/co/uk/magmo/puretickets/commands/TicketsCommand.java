package co.uk.magmo.puretickets.commands;

import co.aikar.commands.annotation.*;
import co.uk.magmo.puretickets.ticket.FutureTicket;
import co.uk.magmo.puretickets.utilities.Constants;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

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
}
