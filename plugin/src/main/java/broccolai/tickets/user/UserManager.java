package broccolai.tickets.user;

import broccolai.tickets.events.AsyncSoulJoinEvent;
import broccolai.tickets.events.TicketCreationEvent;
import broccolai.tickets.locale.LocaleManager;
import broccolai.tickets.storage.SQLQueries;
import broccolai.tickets.tasks.TaskManager;
import broccolai.tickets.utilities.UserUtilities;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The manager for users
 */
public final class UserManager implements Listener {

    private final PluginManager pluginManager;
    private final TaskManager taskManager;
    private final LocaleManager localeManager;
    private final Jdbi jdbi;

    private final Set<String> names;
    private final Map<UUID, PlayerSoul> souls = new HashMap<>();

    /**
     * Construct a user manager
     *
     * @param pluginManager Plugin manager
     * @param taskManager   Task manager
     * @param localeManager Locale manager
     * @param jdbi          Jdbi instance
     */
    public UserManager(
            final @NonNull PluginManager pluginManager,
            final @NonNull TaskManager taskManager,
            final @NonNull LocaleManager localeManager,
            final @NonNull Jdbi jdbi
    ) {
        this.pluginManager = pluginManager;
        this.taskManager = taskManager;
        this.localeManager = localeManager;
        this.jdbi = jdbi;

        this.names = jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.SELECT_UUIDS.get())
                    .mapTo(UUID.class)
                    .map(UserUtilities::nameFromUUID)
                    .collect(Collectors.toSet());
        });
    }

    /**
     * Create a Soul from the Command Sender and register it
     *
     * @param sender Command Sender
     * @return Soul
     */
    public @NonNull Soul fromSender(final @NonNull CommandSender sender) {
        UUID uuid;

        if (sender instanceof ConsoleCommandSender) {
            return new ConsoleSoul(localeManager);
        } else {
            Player player = (Player) sender;
            uuid = player.getUniqueId();
        }

        if (souls.containsKey(uuid)) {
            return souls.get(uuid);
        }

        return makeAndPut((Player) sender);
    }

    /**
     * Create a soul from a player
     *
     * @param player Player instance
     * @return Player soul
     */
    public @NonNull PlayerSoul fromPlayer(final @NonNull Player player) {
        return (PlayerSoul) fromSender(player);
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
        return jdbi.withHandle(handle -> {
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
    public void saveAll() {
        this.souls.values().forEach(this::cleanup);
    }

    private @NonNull PlayerSoul makeAndPut(final @NonNull Player player) {
        PlayerSoul soul = new PlayerSoul(this, localeManager, player);

        souls.put(player.getUniqueId(), soul);
        return soul;
    }

    private void cleanup(final @NonNull PlayerSoul soul) {
        if (!soul.isDirty()) {
            return;
        }

        UserSettings settings = soul.preferences();
        jdbi.useHandle(handle -> {
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
    @EventHandler
    public void onTicketCreation(final @NonNull TicketCreationEvent e) {
        this.names.add(e.getSoul().getName());
    }

    /**
     * Listener for Player Joins
     *
     * @param e Event
     */
    @EventHandler
    public void onJoin(final @NonNull PlayerJoinEvent e) {
        taskManager.async(() -> {
            PlayerSoul soul = fromPlayer(e.getPlayer());
            AsyncSoulJoinEvent event = new AsyncSoulJoinEvent(soul);
            pluginManager.callEvent(event);
        });
    }

    /**
     * Listener for Player Quits
     *
     * @param e Event
     */
    @EventHandler
    public void onQuit(final @NonNull PlayerQuitEvent e) {
        taskManager.async(() -> {
            PlayerSoul soul = this.fromPlayer(e.getPlayer());
            cleanup(soul);
        });
    }

}
