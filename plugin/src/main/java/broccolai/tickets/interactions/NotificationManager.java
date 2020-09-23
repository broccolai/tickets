package broccolai.tickets.interactions;

import broccolai.tickets.commands.CommandManager;
import broccolai.tickets.configuration.Config;
import broccolai.tickets.events.TicketCreationEvent;
import broccolai.tickets.exceptions.PureException;
import broccolai.tickets.integrations.DiscordManager;
import broccolai.tickets.locale.MessageNames;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.locale.TargetType;
import broccolai.tickets.storage.functions.NotificationSQL;
import broccolai.tickets.tasks.ReminderTask;
import broccolai.tickets.tasks.TaskManager;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.user.UserManager;
import broccolai.tickets.utilities.Constants;
import broccolai.tickets.utilities.generic.ReplacementUtilities;
import broccolai.tickets.utilities.generic.TimeUtilities;
import broccolai.tickets.utilities.generic.UserUtilities;
import co.aikar.commands.CommandIssuer;
import com.google.common.collect.Multimap;
import com.google.common.collect.ObjectArrays;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Notification Manager.
 */
public class NotificationManager implements Listener {
    @NotNull
    private final UserManager userManager;
    @NotNull
    private final CommandManager commandManager;
    @NotNull
    private final DiscordManager discordManager;
    @NotNull
    private final Multimap<UUID, PendingNotification> awaiting;

    /**
     * Initialise a Notification Manager.
     *
     * @param config         the config instance
     * @param taskManager    the task manager
     * @param userManager    the user manager
     * @param commandManager the command manager
     * @param discordManager the discord manager
     */
    public NotificationManager(@NotNull Config config, @NotNull TaskManager taskManager, @NotNull UserManager userManager, @NotNull CommandManager commandManager,
                               @NotNull DiscordManager discordManager) {
        this.userManager = userManager;
        this.commandManager = commandManager;
        this.discordManager = discordManager;

        awaiting = NotificationSQL.selectAllAndClear();

        taskManager.addRepeatingTask(new ReminderTask(this),
            TimeUtilities.minuteToLong(config.REMINDER__DELAY), TimeUtilities.minuteToLong(config.REMINDER__REPEAT));
    }

    /**
     * Send a notification to various sources.
     *
     * @param sender the notification initiator
     * @param target the target of the action
     * @param names  the notification type name
     * @param ticket the ticket instance
     */
    public void send(@NotNull CommandSender sender, @Nullable UUID target, @NotNull MessageNames names, @NotNull Ticket ticket) {
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
                    assert target != null;
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
                    UUID uuid = sender instanceof Player ? ((Player) sender).getUniqueId() : null;

                    discordManager.sendInformation(ticket, uuid, names.name());
                    break;

                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * Send a simple localised message to a server user.
     *
     * @param serverOperator the recipient
     * @param messageKey     the locale message key
     * @param replacements   the replacements to use
     */
    public void basic(ServerOperator serverOperator, Messages messageKey, String... replacements) {
        senderAsIssuer(serverOperator).sendInfo(messageKey, replacements);
    }

    /**
     * Handle a PureException and send the result to a CommandSender.
     *
     * @param commandSender the command sender to message
     * @param exception     the PureException to handle
     */
    public void handleException(CommandSender commandSender, PureException exception) {
        if (exception.getValue() != null) {
            commandSender.sendMessage(exception.getValue());
            return;
        }

        senderAsIssuer(commandSender).sendInfo(exception.getMessageKey(), exception.getReplacements());
    }

    /**
     * Save all awaiting notifications.
     */
    public void save() {
        NotificationSQL.insertAll(awaiting);
    }

    /**
     * Event to notify player when a ticket is created.
     */
    @EventHandler
    public void onTicketCreation(TicketCreationEvent e) {
        send(e.getPlayer(), null, MessageNames.NEW_TICKET, e.getTicket());
    }

    /**
     * Event to listen for player joins and send them outstanding notifications.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        CommandIssuer ci = commandManager.getCommandIssuer(e.getPlayer());

        awaiting.get(ci.getUniqueId()).forEach(n -> {
            try {
                ci.sendInfo(n.getMessageKey(), n.getReplacements());
            } catch (IllegalArgumentException ignored) {
                Bukkit.getLogger().warning("Could not send notifications to " + ci.getUniqueId());
            }
        });

        awaiting.removeAll(ci.getUniqueId());
    }

    /**
     * Convert a user to a command issuer.
     *
     * @param operator the user to convert
     * @return the converted CommandIssuer
     */
    private CommandIssuer senderAsIssuer(ServerOperator operator) {
        return commandManager.getCommandIssuer(operator);
    }
}