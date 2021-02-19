package broccolai.tickets.bukkit.user;

import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.events.api.NotificationEvent;
import broccolai.tickets.core.events.api.TicketCreationEvent;
import broccolai.tickets.core.tasks.TaskManager;
import broccolai.tickets.core.user.ConsoleSoul;
import broccolai.tickets.core.user.PlayerSoul;
import broccolai.tickets.core.user.UserManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.event.method.annotation.Subscribe;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class BukkitUserManager extends UserManager<CommandSender, Player, BukkitPlayerSoul> implements Listener {

    private final BukkitAudiences audiences;

    /**
     * Construct a user manager
     *
     * @param plugin       Plugin instance
     * @param eventManager Event manager
     * @param taskManager  Task manager
     * @param jdbi         Jdbi instance
     */
    public BukkitUserManager(
            final @NonNull Plugin plugin,
            final @NonNull TicketsEventBus eventManager,
            final @NonNull TaskManager taskManager,
            final @NonNull Jdbi jdbi
    ) {
        super(eventManager, taskManager, jdbi);
        this.audiences = BukkitAudiences.create(plugin);
        this.initialise();
    }

    @Override
    public @NonNull List<PlayerSoul<CommandSender, Player>> getAllOnlinePlayer() {
        List<PlayerSoul<CommandSender, Player>> output = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            output.add(this.fromPlayer(player));
        }

        return output;
    }

    @Override
    public @NonNull UUID getUniqueId(final @NonNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            return ConsoleSoul.CONSOLE_UUID;
        }

        Player player = (Player) sender;
        return player.getUniqueId();
    }

    @Override
    public @NonNull UUID getUniqueId(@NonNull final String source) {
        @SuppressWarnings("deprecation")
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(source);

        return offlinePlayer.getUniqueId();
    }

    @Override
    public @NonNull Optional<Player> getPlayer(@NonNull final UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    @Override
    public boolean isOnline(@NonNull final UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).isOnline();
    }

    @Override
    public @NonNull Class<BukkitPlayerSoul> getPlayerSoulClass() {
        return BukkitPlayerSoul.class;
    }

    @Override
    protected @NotNull ConsoleSoul<CommandSender> createConsoleSoul() {
        return new BukkitConsoleSoul(this.audiences.console());
    }

    @Override
    protected @NonNull BukkitPlayerSoul makeAndPut(final @NonNull Player player) {
        BukkitPlayerSoul soul = new BukkitPlayerSoul(this, this.audiences.player(player), player);

        this.souls.put(player.getUniqueId(), soul);
        return soul;
    }

    @Override
    protected @NonNull String internalGetName(@NonNull final UUID uuid) {
        String rawName = Bukkit.getOfflinePlayer(uuid).getName();

        return rawName != null ? rawName : "unknown";
    }

    /**
     * @param e Join event
     */
    @EventHandler
    public void onJoin(final @NonNull PlayerJoinEvent e) {
        this.processJoin(e.getPlayer());
    }

    /**
     * @param e Quit event
     */
    @EventHandler
    public void onQuit(final @NotNull PlayerQuitEvent e) {
        this.processQuit(e.getPlayer());
    }

    @Override
    @Subscribe
    public void onTicketCreation(final @NonNull TicketCreationEvent e) {
        super.onTicketCreation(e);
    }

    @Override
    @Subscribe
    public void onNotification(final @NonNull NotificationEvent e) {
        super.onNotification(e);
    }

}
