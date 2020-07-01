package co.uk.magmo.puretickets.commands;

import co.aikar.commands.annotation.*;
import co.uk.magmo.puretickets.ticket.FutureTicket;
import co.uk.magmo.puretickets.utilities.Constants;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("ticket|ti")
@CommandPermission(Constants.USER_PERMISSION)
public class TicketCommand extends PureBaseCommand {
    @Subcommand("%show")
    @CommandCompletion("@IssuerIds")
    @CommandPermission(Constants.USER_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("[Index]")
    public void onShow(Player player, @Optional @Flags("issuer") FutureTicket future) {
        processShowCommand(getCurrentCommandIssuer(), future);
    }
}
