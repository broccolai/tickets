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
import broccolai.tickets.storage.SQLQueries;
import broccolai.tickets.tasks.ReminderTask;
import broccolai.tickets.tasks.TaskManager;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.user.PlayerSoul;
import broccolai.tickets.user.Soul;
import broccolai.tickets.user.UserManager;
import broccolai.tickets.utilities.Constants;
import broccolai.tickets.utilities.ReplacementUtilities;
import broccolai.tickets.utilities.TimeUtilities;
import broccolai.tickets.utilities.UserUtilities;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ObjectArrays;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.util.UUID;

public final class NotificationManager implements Listener {

    private final Jdbi jdbi;
    private final LocaleManager localeManager;
    private final UserManager userManager;
    private final DiscordManager discordManager;

    private final Multimap<UUID, String> pendingNotifications = ArrayListMultimap.create();

    /**
     * Initialise a Notification Manager
     *
     * @param jdbi           Jdbi instance
     * @param config         Config instance
     * @param taskManager    Task manager
     * @param userManager    User manager
     * @param discordManager Discord manager
     * @param localeManager  Locale manager
     * @param ticketManager  Ticket Manager
     */
    public NotificationManager(
            final @NonNull Jdbi jdbi,
            final @NonNull Config config,
            final @NonNull TaskManager taskManager,
            final @NonNull LocaleManager localeManager,
            final @NonNull UserManager userManager,
            final @NonNull DiscordManager discordManager,
            final @NonNull TicketManager ticketManager
    ) {
        this.jdbi = jdbi;
        this.localeManager = localeManager;
        this.userManager = userManager;
        this.discordManager = discordManager;

        taskManager.addRepeatingTask(new ReminderTask(userManager, ticketManager),
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
            final @NonNull Soul soul,
            final @Nullable UUID target,
            final @NonNull MessageNames names,
            final @NonNull Ticket ticket
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
                        this.userManager.fromPlayer((Player) op).message(message, replacements);
                    } else {
                        this.pendingNotifications.put(
                                target,
                                this.localeManager.composeMessage(message, replacements).complete()
                        );
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
                    this.discordManager.sendInformation(ticket, soul.getUniqueId(), names.name());
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
    public void handleException(final @NonNull Soul soul, final @NonNull PureException exception) {
        if (exception.getValue() != null) {
            soul.message(exception.getValue());
            return;
        }

        assert exception.getMessageKey() != null;
        soul.message(exception.getMessageKey(), exception.getReplacements());
    }

    /**
     * Insert all pending notifications into storage
     */
    public void saveAll() {
        this.jdbi.useHandle(handle -> {
            PreparedBatch batch = handle.prepareBatch(SQLQueries.INSERT_NOTIFICATION.get());

            pendingNotifications.forEach((uuid, string) -> {
                batch
                        .bind("uuid", uuid)
                        .bind("message", string)
                        .add();
            });

            batch.execute();
        });
    }

    /**
     * Event to notify player when a ticket is created
     *
     * @param e Event
     */
    @EventHandler
    public void onTicketCreation(final @NonNull TicketCreationEvent e) {
        this.send(e.getSoul(), null, MessageNames.NEW_TICKET, e.getTicket());
    }

    /**
     * Event to listen for player joins and send them outstanding notifications
     *
     * @param e Event
     */
    @EventHandler
    public void onAsyncSoulJoin(final @NonNull AsyncSoulJoinEvent e) {
        final Soul soul = e.getSoul();

        this.jdbi.useHandle(handle -> {
            handle.createQuery(SQLQueries.SELECT_NOTIFICATIONS.get())
                    .bind("uuid", soul.getUniqueId())
                    .mapTo(String.class)
                    .forEach(soul::message);

            this.pendingNotifications
                    .removeAll(soul.getUniqueId())
                    .forEach(soul::message);

            handle.createUpdate(SQLQueries.DELETE_NOTIFICATIONS.get())
                    .bind("uuid", soul.getUniqueId())
                    .execute();
        });
    }

}
