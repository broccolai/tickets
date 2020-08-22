package broccolai.tickets.interactions;

import broccolai.tickets.commands.CommandManager;
import broccolai.tickets.configuration.Config;
import broccolai.tickets.integrations.DiscordManager;
import broccolai.tickets.locale.MessageNames;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.locale.TargetType;
import broccolai.tickets.storage.SQLManager;
import broccolai.tickets.tasks.ReminderTask;
import broccolai.tickets.tasks.TaskManager;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.user.UserManager;
import broccolai.tickets.utilities.Constants;
import broccolai.tickets.utilities.generic.ReplacementUtilities;
import broccolai.tickets.utilities.generic.TimeUtilities;
import broccolai.tickets.utilities.generic.UserUtilities;
import co.aikar.commands.CommandIssuer;
import com.google.common.collect.Multimap;
import com.google.common.collect.ObjectArrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.ServerOperator;

public class NotificationManager implements Listener {
    private final SQLManager sqlManager;
    private final UserManager userManager;
    private final CommandManager commandManager;
    private final DiscordManager discordManager;

    private final Multimap<UUID, PendingNotification> awaiting;

    public NotificationManager(Config config, SQLManager sqlManager, TaskManager taskManager, UserManager userManager, CommandManager commandManager, DiscordManager discordManager,
                               TicketManager ticketManager) {
        this.sqlManager = sqlManager;
        this.userManager = userManager;
        this.commandManager = commandManager;
        this.discordManager = discordManager;

        awaiting = sqlManager.getNotification().selectAllAndClear();

        taskManager.addRepeatingTask(new ReminderTask(ticketManager, this),
            TimeUtilities.minuteToLong(config.REMINDER__DELAY), TimeUtilities.minuteToLong(config.REMINDER__REPEAT));
    }

    public void send(CommandSender sender, UUID target, MessageNames names, Ticket ticket, Consumer<HashMap<String, String>> addFields) {
        String[] specificReplacements = {"%user%", sender.getName(), "%target%", UserUtilities.nameFromUUID(target)};
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
                        if (!player.hasPermission(Constants.STAFF_PERMISSION + ".announce")) {
                            continue;
                        }
                        if (!userManager.get(player.getUniqueId()).getAnnouncements()) {
                            continue;
                        }
                        if (sender instanceof Player && ((Player) sender).getUniqueId() == player.getUniqueId()) {
                            continue;
                        }

                        senderAsIssuer(player).sendInfo(message, replacements);
                    }

                    break;

                case DISCORD:
                    HashMap<String, String> fields = new HashMap<>();

                    if (addFields != null) {
                        addFields.accept(fields);
                    }

                    UUID uuid = sender instanceof Player ? ((Player) sender).getUniqueId() : null;

                    discordManager.sendInformation(ticket, uuid, names.name());
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

        awaiting.get(ci.getUniqueId()).forEach(n -> {
            try {
                ci.sendInfo(n.getMessageKey(), n.getReplacements());
            } catch (IllegalArgumentException ignored) { }
        });

        awaiting.removeAll(ci.getUniqueId());
    }

    private CommandIssuer senderAsIssuer(ServerOperator operator) {
        return commandManager.getCommandIssuer(operator);
    }
}