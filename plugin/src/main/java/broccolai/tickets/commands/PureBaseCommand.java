package broccolai.tickets.commands;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.tasks.TaskManager;
import broccolai.tickets.ticket.FutureTicket;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.utilities.generic.ReplacementUtilities;
import broccolai.tickets.utilities.generic.TimeUtilities;
import broccolai.tickets.utilities.generic.UserUtilities;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.HelpCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.SimplePluginManager;

/**
 * Base commands for ticket commands inherit from.
 */
public class PureBaseCommand extends BaseCommand {
    @Dependency
    protected Config config;
    @Dependency
    protected SimplePluginManager pluginManager;
    @Dependency
    protected NotificationManager notificationManager;
    @Dependency
    protected TicketManager ticketManager;
    @Dependency
    protected TaskManager taskManager;
    @Dependency
    protected TicketSQL ticketSQL;

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    protected void processShowCommand(CommandIssuer issuer, FutureTicket future) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) -> {
                String[] replacements = ReplacementUtilities.ticketReplacements(ticket);

                issuer.sendInfo(Messages.TITLES__SHOW_TICKET, replacements);
                issuer.sendInfo(Messages.SHOW__SENDER, replacements);
                issuer.sendInfo(Messages.SHOW__MESSAGE, replacements);
                issuer.sendInfo(Messages.SHOW__LOCATION, replacements);

                if (ticket.getStatus() != TicketStatus.PICKED) {
                    issuer.sendInfo(Messages.SHOW__UNPICKED);
                } else {
                    issuer.sendInfo(Messages.SHOW__PICKER, replacements);
                }
            })
            .execute();
    }

    protected void processLogCommand(CommandIssuer issuer, FutureTicket future) {
        taskManager.use()
            .future(future)
            .abortIfNull()
            .asyncLast((ticket) -> {
                String[] replacements = ReplacementUtilities.ticketReplacements(ticket);

                issuer.sendInfo(Messages.TITLES__TICKET_LOG, replacements);

                ticket.getMessages().forEach(message -> {
                    String suffix = message.getData() != null ? message.getData() : UserUtilities.nameFromUUID(message.getSender());

                    issuer.sendInfo(Messages.GENERAL__LOG_FORMAT, "%reason%", message.getReason().name(),
                        "%date%", TimeUtilities.formatted(message.getDate()), "%suffix%", suffix);
                });
            })
            .execute();
    }
}
