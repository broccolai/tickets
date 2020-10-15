package broccolai.tickets.user;

import broccolai.tickets.events.AsyncSoulJoinEvent;
import broccolai.tickets.events.TicketCreationEvent;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.tasks.TaskManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The manager for users
 */
public final class UserManager implements Listener {

    @NonNull
    private final PluginManager pluginManager;
    @NonNull
    private final TaskManager taskManager;
    @NonNull
    private final Map<UUID, PlayerSoul> souls = new HashMap<>();
    @NonNull
    private final Set<String> names = TicketSQL.selectNames();

    /**
     * Construct a user manager
     *
     * @param pluginManager Plugin manager
     * @param taskManager   Task manager
     */
    public UserManager(@NonNull final PluginManager pluginManager, @NonNull final TaskManager taskManager) {
        this.pluginManager = pluginManager;
        this.taskManager = taskManager;
    }

    /**
     * Create a Soul from the Command Sender and register it
     *
     * @param sender Command Sender
     * @return Soul
     */
    @NonNull
    public Soul fromSender(@NonNull final CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return new ConsoleSoul();
        }

        Player player = (Player) sender;

        if (souls.containsKey(player.getUniqueId())) {
            return souls.get(player.getUniqueId());
        }

        return makeAndPut(player);
    }

    /**
     * Create a soul from a player
     *
     * @param player Player instance
     * @return Player soul
     */
    @NonNull
    public PlayerSoul fromPlayer(@NonNull final Player player) {
        return (PlayerSoul) fromSender(player);
    }

    /**
     * Get all cached names
     *
     * @return List of names
     */
    @NonNull
    public List<String> getNames() {
        return new ArrayList<>(names);
    }

    @NonNull
    private PlayerSoul makeAndPut(@NonNull final Player player) {
        PlayerSoul soul = new PlayerSoul(player);

        souls.put(player.getUniqueId(), soul);
        return soul;
    }

    /**
     * Listener for Ticket Creation
     *
     * @param e Event
     */
    @EventHandler
    public void onTicketCreation(@NonNull final TicketCreationEvent e) {
        names.add(e.getSoul().getName());
    }

    /**
     * Listener for Player Joins
     *
     * @param e Event
     */
    @EventHandler
    public void onJoin(@NonNull final PlayerJoinEvent e) {
        taskManager.async(() -> {
            PlayerSoul soul = fromPlayer(e.getPlayer());
            AsyncSoulJoinEvent event = new AsyncSoulJoinEvent(soul);
            pluginManager.callEvent(event);
        });
    }

}
