package broccolai.tickets.core.user;

import broccolai.tickets.core.events.Event;
import broccolai.tickets.core.events.EventListener;
import broccolai.tickets.core.events.api.NotificationEvent;
import broccolai.tickets.core.events.api.SoulJoinEvent;
import broccolai.tickets.core.events.api.TicketCreationEvent;
import broccolai.tickets.core.storage.SQLQueries;
import broccolai.tickets.core.tasks.TaskManager;
import broccolai.tickets.core.utilities.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.event.EventBus;
import net.kyori.event.method.annotation.Subscribe;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @param <C> Sender Type
 * @param <P> Player Type
 * @param <S> Player Soul Type
 */
public abstract class UserManager<C, P extends C, S extends PlayerSoul<C, P>> implements EventListener {

    protected final EventBus<Event> eventManager;
    protected final TaskManager taskManager;
    protected final Jdbi jdbi;

    protected final Map<UUID, Soul<C>> souls = new HashMap<>();
    protected final Map<UUID, String> uuidName = new HashMap<>();
    protected Set<String> names;

    /**
     * Construct a user manager
     *
     * @param eventManager  Event manager
     * @param taskManager   Task manager
     * @param jdbi          Jdbi instance
     */
    public UserManager(
            final @NonNull EventBus<Event> eventManager,
            final @NonNull TaskManager taskManager,
            final @NonNull Jdbi jdbi
    ) {
        this.eventManager = eventManager;
        this.taskManager = taskManager;
        this.jdbi = jdbi;
    }

    /**
     * Initialise user manager fields
     */
    protected void initialise() {
        this.names = this.jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.SELECT_UUIDS.get())
                    .mapTo(UUID.class)
                    .map(this::getName)
                    .collect(Collectors.toSet());
        });

        this.souls.put(ConsoleSoul.CONSOLE_UUID, this.createConsoleSoul());
    }

    /**
     * Get all the currently online players, as souls
     *
     * @return List of player souls
     */
    public abstract @NonNull List<PlayerSoul<C, P>> getAllOnlinePlayer();

    /**
     * Get the unique id of {@link C}
     *
     * @param sender {@link C} to get sender from
     * @return Unique id
     */
    public abstract @NonNull UUID getUniqueId(@NonNull C sender);

    /**
     * Get the unique id related to a target name
     *
     * @param source Sources name
     * @return Unique id
     */
    public abstract @NonNull UUID getUniqueId(@NonNull String source);

    /**
     * Get a player associated to a unique id
     *
     * @param uuid Unique id of player
     * @return Optional wrap of player
     */
    public abstract @NonNull Optional<P> getPlayer(@NonNull UUID uuid);

    /**
     * Check if a players uuid is online
     *
     * @param uuid Unique id
     * @return true if online
     */
    public abstract boolean isOnline(@NonNull UUID uuid);

    /**
     * Get the implementations class of player soul
     *
     * @return Class of player soul
     */
    public abstract @NonNull Class<S> getPlayerSoulClass();

    /**
     * Create a console soul
     *
     * @return Console soul
     */
    protected abstract @NotNull ConsoleSoul<C> createConsoleSoul();

    protected abstract @NonNull S makeAndPut(@NonNull P player);

    protected abstract @NonNull String internalGetName(@NonNull UUID uuid);

    /**
     * Create a Soul from the Command Sender and register it
     *
     * @param sender Command Sender
     * @return Soul
     */
    @SuppressWarnings("unchecked")
    public @NonNull Soul<C> fromSender(final @NonNull C sender) {
        UUID uuid = this.getUniqueId(sender);

        if (this.souls.containsKey(uuid)) {
            return this.souls.get(uuid);
        }

        return this.makeAndPut((P) sender);
    }

    /**
     * Create a soul from a player
     *
     * @param player Player instance
     * @return Player soul
     */
    @SuppressWarnings("unchecked")
    public @NonNull S fromPlayer(final @NonNull P player) {
        return (S) this.fromSender(player);
    }

    /**
     * Try to get a soul from a unique id
     *
     * @param uuid Unique id
     * @return Optional soul
     */
    public @NonNull Optional<S> fromUniqueId(final @NonNull UUID uuid) {
        return this.getPlayer(uuid).map(this::fromPlayer);
    }

    public User getUser(final @NonNull UUID uuid) {
        return new User.Simple(uuid, this.getName(uuid));
    }

    /**
     * Get the name associated with a a unique id
     *
     * @param uuid Unique id
     * @return Get the name linked
     */
    public final @NonNull String getName(final @NonNull UUID uuid) {
        if (this.uuidName.containsKey(uuid)) {
            return this.uuidName.get(uuid);
        }

        return this.internalGetName(uuid);
    }

    /**
     * Get all cached names
     *
     * @return List of names
     */
    public @NonNull List<String> getNames() {
        return new ArrayList<>(names);
    }

    /**
     * Load a users settings from storage
     *
     * @param uuid Unique id
     * @return UserSettings object
     */
    public @NonNull UserSettings loadSettings(final @NonNull UUID uuid) {
        return this.jdbi.withHandle(handle -> {
            boolean exists = handle.createQuery(SQLQueries.EXISTS_SETTINGS.get())
                    .bind("uuid", uuid)
                    .mapTo(Boolean.class)
                    .first();

            if (exists) {
                return handle.createQuery(SQLQueries.SELECT_SETTINGS.get())
                        .bind("uuid", uuid)
                        .mapTo(UserSettings.class)
                        .first();
            }

            UserSettings settings = new UserSettings(true);

            handle.createUpdate(SQLQueries.INSERT_SETTINGS.get())
                    .bind("uuid", uuid)
                    .bind("announcements", settings.getAnnouncements())
                    .execute();

            return settings;
        });
    }

    /**
     * Save all cached souls
     */
    @SuppressWarnings("unchecked")
    public void saveAll() {
        this.souls
                .values()
                .stream()
                .filter(soul -> soul instanceof PlayerSoul)
                .map(soul -> (S) soul)
                .forEach(this::save);

        this.souls.clear();
    }

    protected final void save(final @NonNull S soul) {
        if (!soul.isDirty()) {
            return;
        }

        UserSettings settings = soul.preferences();
        this.jdbi.useHandle(handle -> {
            handle.createUpdate(SQLQueries.UPDATE_SETTINGS.get())
                    .bind("uuid", soul.getUniqueId())
                    .bind("announcements", settings.getAnnouncements())
                    .execute();
        });
    }

    /**
     * Listener for Ticket Creation
     *
     * @param e Event
     */
    @Subscribe
    public void onTicketCreation(final @NonNull TicketCreationEvent e) {
        this.names.add(e.getSoul().getName());
    }

    /**
     * @param e Notification event
     */
    @Subscribe
    public void onNotification(final @NonNull NotificationEvent e) {
        if (!e.getReason().announcement().isPresent()) {
            return;
        }

        Component component = e.getReason().announcement().get().use(e.getTemplates());

        for (final PlayerSoul<?, ?> soul : this.getAllOnlinePlayer()) {
            if (e.getSender() == soul
                    || !soul.preferences().getAnnouncements()
                    || !soul.hasPermission(Constants.STAFF_PERMISSION + ".announce")) {
                continue;
            }

            soul.sendMessage(component);
        }
    }

    /**
     * @param player Player
     */
    protected void processJoin(final @NonNull P player) {
        this.taskManager.async(() -> {
            S soul = this.fromPlayer(player);
            SoulJoinEvent event = new SoulJoinEvent(soul);
            this.eventManager.post(event);
        });
    }

    /**
     * @param player Player
     */
    protected void processQuit(final @NonNull P player) {
        this.taskManager.async(() -> {
            S soul = this.fromPlayer(player);
            this.save(soul);
            this.souls.remove(soul.getUniqueId());
        });
    }

}
