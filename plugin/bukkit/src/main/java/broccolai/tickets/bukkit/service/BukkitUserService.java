package broccolai.tickets.bukkit.service;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.bukkit.model.BukkitConsoleSoul;
import broccolai.tickets.bukkit.model.BukkitOfflineSoul;
import broccolai.tickets.bukkit.model.BukkitPlayerSoul;
import broccolai.tickets.bukkit.service.snapshot.BukkitSnapshotService;
import broccolai.tickets.core.service.user.SimpleUserService;
import broccolai.tickets.core.utilities.ClassHelper;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class BukkitUserService extends SimpleUserService {

    private final AudienceProvider audienceProvider;

    @Inject
    public BukkitUserService(
            final @NonNull Injector injector,
            final @NonNull AudienceProvider audienceProvider,
            final @NonNull BukkitSnapshotService bukkitSnapshotService,
            final @NonNull BukkitSnapshotService paperSnapshotService
    ) {
        super(injector);
        this.audienceProvider = audienceProvider;

        this.registerSnapshotService(bukkitSnapshotService);

        if (ClassHelper.classExists("com.destroystokyo.paper.profile.PlayerProfile")) {
            this.registerSnapshotService(paperSnapshotService);
        }
    }

    public @NonNull PlayerSoul player(final @NonNull Player player) {
        return new BukkitPlayerSoul(player, this.audienceProvider.player(player.getUniqueId()));
    }

    @Override
    public @NonNull ConsoleSoul console() {
        return new BukkitConsoleSoul(this.audienceProvider.console());
    }

    @Override
    public @NonNull PlayerSoul player(final @NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            throw new IllegalArgumentException();
        }

        return new BukkitPlayerSoul(player, this.audienceProvider.player(uuid));
    }

    @Override
    public @NonNull Soul offlinePlayer(final @NonNull UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        return new BukkitOfflineSoul(offlinePlayer);
    }

    @Override
    public @NonNull Collection<PlayerSoul> players() {
        Collection<PlayerSoul> players = new ArrayList<>();

        for (final Player player : Bukkit.getOnlinePlayers()) {
            players.add(this.player(player.getUniqueId()));
        }

        return players;
    }

    @Override
    public @NonNull String name(final @NonNull UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NonNull UUID uuidFromName(final @NonNull String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId();
    }

    @Override
    protected boolean isOnline(final @NonNull UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).isOnline();
    }

}
