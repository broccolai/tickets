package broccolai.tickets.interactions;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.events.AsyncSoulJoinEvent;
import broccolai.tickets.events.TicketCreationEvent;
import broccolai.tickets.exceptions.PureException;
import broccolai.tickets.integrations.DiscordManager;
import broccolai.tickets.locale.LocaleManager;
import broccolai.tickets.locale.MessageNames;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.locale.TargetType;
import broccolai.tickets.storage.functions.NotificationSQL;
import broccolai.tickets.tasks.ReminderTask;
import broccolai.tickets.tasks.TaskManager;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.user.PlayerSoul;
import broccolai.tickets.user.Soul;
import broccolai.tickets.user.UserManager;
import broccolai.tickets.utilities.Constants;
import broccolai.tickets.utilities.ReplacementUtilities;
import broccolai.tickets.utilities.TimeUtilities;
import broccolai.tickets.utilities.UserUtilities;
import com.google.common.collect.ObjectArrays;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Notification Manager
 */
public final class NotificationManager implements Listener {

    @NonNull
    private final LocaleManager localeManager;
    @NonNull
    private final UserManager userManager;
    @NonNull
    private final DiscordManager discordManager;

    /**
     * Initialise a Notification Manager
     *
     * @param config         the config instance
     * @param taskManager    the task manager
     * @param userManager    the user manager
     * @param discordManager the discord manager
     * @param localeManager  the locale manager
     */
    public NotificationManager(
            @NonNull final Config config,
            @NonNull final TaskManager taskManager,
            @NonNull final LocaleManager localeManager,
            @NonNull final UserManager userManager,
            @NonNull final DiscordManager discordManager
    ) {
        this.localeManager = localeManager;
        this.userManager = userManager;
        this.discordManager = discordManager;

        taskManager.addRepeatingTask(new ReminderTask(userManager),
                TimeUtilities.minuteToLong(config.getReminderDelay()), TimeUtilities.minuteToLong(config.getReminderRepeat())
        );
    }

    /**
     * Send a notification to various sources
     *
     * @param soul   the notification initiator
     * @param target the target of the action
     * @param names  the notification type name
     * @param ticket the ticket instance
     */
    public void send(
            @NonNull final Soul soul,
            @Nullable final UUID target,
            @NonNull final MessageNames names,
            @NonNull final Ticket ticket
    ) {
        String[] specificReplacements = {"user", soul.getName(), "target", UserUtilities.nameFromUUID(target)};
        String[] genericReplacements = ReplacementUtilities.ticketReplacements(ticket);
        String[] replacements = ObjectArrays.concat(specificReplacements, genericReplacements, String.class);

        for (TargetType targetType : names.getTargets()) {
            switch (targetType) {
                case SENDER:
                    soul.message(Messages.retrieve(targetType, names), replacements);
                    break;

                case NOTIFICATION:
                    Messages message = Messages.retrieve(targetType, names);

                    assert target != null;
                    OfflinePlayer op = Bukkit.getOfflinePlayer(target);

                    if (op.isOnline()) {
                        userManager.fromPlayer((Player) op).message(message, replacements);
                    } else {
                        NotificationSQL.add(target, localeManager.composeMessage(message, replacements).complete());
                    }

                    break;

                case ANNOUNCEMENT:
                    for (final Player player : Bukkit.getOnlinePlayers()) {
                        if (!player.hasPermission(Constants.STAFF_PERMISSION + ".announce")) {
                            continue;
                        }

                        final PlayerSoul playerSoul = userManager.fromPlayer(player);
                        if (!playerSoul.preferences().getAnnouncements()) {
                            continue;
                        }

                        if (soul == playerSoul) {
                            continue;
                        }

                        playerSoul.message(Messages.retrieve(targetType, names), replacements);
                    }

                    break;

                case DISCORD:
                    discordManager.sendInformation(ticket, soul.getUniqueId(), names.name());
                    break;

                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * Handle a PureException and send the result to a CommandSender
     *
     * @param soul      the command sender to message
     * @param exception the PureException to handle
     */
    public void handleException(@NonNull final Soul soul, @NonNull final PureException exception) {
        if (exception.getValue() != null) {
            soul.message(exception.getValue());
            return;
        }

        assert exception.getMessageKey() != null;
        soul.message(exception.getMessageKey(), exception.getReplacements());
    }

    /**
     * Event to notify player when a ticket is created
     *
     * @param e Event
     */
    @EventHandler
    public void onTicketCreation(@NonNull final TicketCreationEvent e) {
        send(e.getSoul(), null, MessageNames.NEW_TICKET, e.getTicket());
    }

    /**
     * Event to listen for player joins and send them outstanding notifications
     *
     * @param e Event
     */
    @EventHandler
    public void onAsyncSoulJoin(@NonNull final AsyncSoulJoinEvent e) {
        final Soul soul = e.getSoul();

        NotificationSQL.retrieve(soul.getUniqueId())
                .forEach(soul::message);
    }

}
