package broccolai.tickets.core.interactions;

import broccolai.tickets.core.events.EventListener;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.events.api.NotificationEvent;
import broccolai.tickets.core.events.api.SoulJoinEvent;
import broccolai.tickets.core.events.api.TicketCreationEvent;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.storage.SQLQueries;
import broccolai.tickets.core.user.PlayerSoul;
import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.UserManager;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.event.method.annotation.Subscribe;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.util.Optional;
import java.util.UUID;

public final class NotificationManager<S extends PlayerSoul<?, ?>> implements EventListener {

    private static final MiniMessage MINI = MiniMessage.get();

    private final Jdbi jdbi;
    private final TicketsEventBus eventBus;
    private final UserManager<?, ?, S> userManager;

    private final Multimap<UUID, String> pendingNotifications = ArrayListMultimap.create();

    /**
     * Initialise a Notification Manager
     *
     * @param jdbi        Jdbi instance
     * @param eventBus    Event bus
     * @param userManager User manager
     */
    public NotificationManager(
            final @NonNull Jdbi jdbi,
            final @NonNull TicketsEventBus eventBus,
            final @NonNull UserManager<?, ?, S> userManager
    ) {
        this.jdbi = jdbi;
        this.eventBus = eventBus;
        this.userManager = userManager;
    }

    /**
     * Handle a PureException and send the result to a CommandSender
     *
     * @param soul      the command sender to message
     * @param exception the PureException to handle
     */
    public void handleException(final @NonNull Soul<?> soul, final @NonNull PureException exception) {
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

            this.pendingNotifications.forEach((uuid, string) -> batch
                    .bind("uuid", uuid)
                    .bind("message", string)
                    .add()
            );

            batch.execute();
        });
    }

    /**
     * Event to notify player when a ticket is created
     *
     * @param e Event
     */
    @Subscribe
    public void onTicketCreation(final @NonNull TicketCreationEvent e) {
        this.eventBus.post(new NotificationEvent(NotificationReason.NEW_TICKET, e.getSoul(), null, e.getTicket()));
    }

    /**
     * @param e Notification event
     */
    @Subscribe
    public void onSenderNotification(final @NonNull NotificationEvent e) {
        if (!e.getReason().sender().isPresent()) {
            return;
        }

        Component component = e.getReason().sender().get().use(e.getTemplates());
        e.getSender().sendMessage(component);
    }

    /**
     * @param e Notification event
     */
    @Subscribe
    public void onNotification(final @NonNull NotificationEvent e) {
        if (!e.getReason().notifies().isPresent() || !e.getTarget().isPresent()) {
            return;
        }

        UUID target = e.getTarget().get();
        Optional<S> optionalSoul = this.userManager.fromUniqueId(target);

        Component component = e.getReason().notifies().get().use(e.getTemplates());

        if (optionalSoul.isPresent()) {
            optionalSoul.get().message(component);
            return;
        }

        this.pendingNotifications.put(target, MINI.serialize(component));
    }

    /**
     * Event to listen for player joins and send them outstanding notifications
     *
     * @param e Event
     */
    @Subscribe
    public void onAsyncSoulJoin(final @NonNull SoulJoinEvent e) {
        final Soul<?> soul = e.getSoul();

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
