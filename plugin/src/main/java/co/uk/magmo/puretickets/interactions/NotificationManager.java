package co.uk.magmo.puretickets.interactions;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.MessageType;
import co.uk.magmo.puretickets.commands.CommandManager;
import co.uk.magmo.puretickets.configuration.Config;
import co.uk.magmo.puretickets.integrations.DiscordManager;
import co.uk.magmo.puretickets.locale.MessageNames;
import co.uk.magmo.puretickets.locale.Messages;
import co.uk.magmo.puretickets.locale.TargetType;
import co.uk.magmo.puretickets.storage.SQLManager;
import co.uk.magmo.puretickets.tasks.ReminderTask;
import co.uk.magmo.puretickets.tasks.TaskManager;
import co.uk.magmo.puretickets.ticket.Ticket;
import co.uk.magmo.puretickets.ticket.TicketManager;
import co.uk.magmo.puretickets.user.UserManager;
import co.uk.magmo.puretickets.utilities.Constants;
import co.uk.magmo.puretickets.utilities.generic.ReplacementUtilities;
import co.uk.magmo.puretickets.utilities.generic.TimeUtilities;
import co.uk.magmo.puretickets.utilities.generic.UserUtilities;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ObjectArrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.ServerOperator;
import java.util.*;
import java.util.function.Consumer;

public class NotificationManager implements Listener {
    private final Config config;
    private final SQLManager sqlManager;
    private final UserManager userManager;
    private final CommandManager commandManager;
    private final DiscordManager discordManager;

    private final ArrayListMultimap<UUID, PendingNotification> awaiting;

    public NotificationManager(Config config, SQLManager sqlManager, TaskManager taskManager, UserManager userManager, CommandManager commandManager, DiscordManager discordManager, TicketManager ticketManager) {
        this.config = config;
        this.sqlManager = sqlManager;
        this.userManager = userManager;
        this.commandManager = commandManager;
        this.discordManager = discordManager;

        awaiting = sqlManager.getNotification().selectAllAndClear();

        taskManager.addRepeatingTask(new ReminderTask(ticketManager, this),
                TimeUtilities.minuteToLong(config.REMINDER__DELAY), TimeUtilities.minuteToLong(config.REMINDER__REPEAT));
    }

    public void send(CommandSender sender, UUID target, MessageNames names, Ticket ticket, Consumer<HashMap<String, String>> addFields) {
        String[] specificReplacements = { "%user%", sender.getName(), "%target%", UserUtilities.nameFromUUID(target) };
        String[] genericReplacements = ReplacementUtilities.ticketReplacements(ticket);

        String[] replacements = ObjectArrays.concat(specificReplacements, genericReplacements, String.class);

        for (TargetType targetType : names.getTargets()) {
            Messages message = Messages.retrieve(targetType, names);

            switch (targetType) {
                case SENDER:
                    senderAsIssuer(sender).sendInfo(message, replacements);
                    break;

               case NOTIFICATION:
                    OfflinePlayer op = Bukkit.getOfflinePlayer(target);

                    if (op.isOnline()) {
                        senderAsIssuer(op).sendInfo(message, replacements);
                    } else {
                        awaiting.put(target, new PendingNotification(message, replacements));
                    }

                    break;

               case ANNOUNCEMENT:
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!player.hasPermission(Constants.STAFF_PERMISSION + ".announce")) continue;
                        if (!userManager.get(player.getUniqueId()).getAnnouncements()) continue;
                        if (sender instanceof Player && ((Player) sender).getUniqueId() == player.getUniqueId()) continue;

                        senderAsIssuer(player).sendInfo(message, replacements);
                    }

                    break;

               case DISCORD:
                    HashMap<String, String> fields = new HashMap<>();

                    if (addFields != null) {
                        addFields.accept(fields);
                    }

                    String action = commandManager.formatMessage(senderAsIssuer(Bukkit.getConsoleSender()), MessageType.INFO, message);
                    action = ChatColor.stripColor(action);

                    discordManager.sendInformation(ticket.getStatus().getPureColor().getHex(),
                            UserUtilities.nameFromUUID(ticket.getPlayerUUID()), ticket.getId(), action, fields);
            }
        }
    }

    public void basic(CommandSender commandSender, Messages messageKey, String... replacements) {
        senderAsIssuer(commandSender).sendInfo(messageKey, replacements);
    }

    public void save() {
        sqlManager.getNotification().insertAll(awaiting);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        CommandIssuer ci = commandManager.getCommandIssuer(e.getPlayer());

        awaiting.get(ci.getUniqueId()).forEach(n -> ci.sendInfo(n.getMessageKey(), n.getReplacements()));

        awaiting.removeAll(ci.getUniqueId());
    }

    private CommandIssuer senderAsIssuer(ServerOperator operator) {
        return commandManager.getCommandIssuer(operator);
    }
}