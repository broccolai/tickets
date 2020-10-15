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
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    private final PluginManager pluginManager;
    @NotNull
    private final TaskManager taskManager;
    @NotNull
    private final Map<UUID, PlayerSoul> souls = new HashMap<>();
    @NotNull
    private final Set<String> names = TicketSQL.selectNames();

    /**
     * Construct a user manager
     *
     * @param pluginManager Plugin manager
     * @param taskManager   Task manager
     */
    public UserManager(@NotNull final PluginManager pluginManager, @NotNull final TaskManager taskManager) {
        this.pluginManager = pluginManager;
        this.taskManager = taskManager;
    }

    /**
     * Create a Soul from the Command Sender and register it
     *
     * @param sender Command Sender
     * @return Soul
     */
    @NotNull
    public Soul fromSender(@NotNull final CommandSender sender) {
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
    @NotNull
    public PlayerSoul fromPlayer(@NotNull final Player player) {
        return (PlayerSoul) fromSender(player);
    }

    /**
     * Get all cached names
     *
     * @return List of names
     */
    @NotNull
    public List<String> getNames() {
        return new ArrayList<>(names);
    }

    @NotNull
    private PlayerSoul makeAndPut(@NotNull final Player player) {
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
    public void onTicketCreation(@NotNull final TicketCreationEvent e) {
        names.add(e.getSoul().getName());
    }

    /**
     * Listener for Player Joins
     *
     * @param e Event
     */
    @EventHandler
    public void onJoin(@NotNull final PlayerJoinEvent e) {
        taskManager.async(() -> {
            PlayerSoul soul = fromPlayer(e.getPlayer());
            AsyncSoulJoinEvent event = new AsyncSoulJoinEvent(soul);
            pluginManager.callEvent(event);
        });
    }

}
