package broccolai.tickets.bukkit.service;

import broccolai.tickets.api.model.user.ConsoleUser;
import broccolai.tickets.api.model.user.PlayerUser;
import broccolai.tickets.bukkit.model.BukkitConsoleUser;
import broccolai.tickets.bukkit.model.BukkitPlayerUser;
import broccolai.tickets.bukkit.service.snapshot.BukkitSnapshotService;
import broccolai.tickets.bukkit.service.snapshot.PaperSnapshotService;
import broccolai.tickets.core.service.user.SimpleUserService;
import broccolai.tickets.core.service.user.snapshot.AshconSnapshotService;
import broccolai.tickets.core.service.user.snapshot.CacheSnapshotService;
import broccolai.tickets.core.service.user.snapshot.DatabaseSnapshotService;
import broccolai.tickets.core.utilities.ReflectionHelper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class BukkitUserService extends SimpleUserService {

    private final AudienceProvider audienceProvider;

    @Inject
    public BukkitUserService(
            final @NonNull AudienceProvider audienceProvider,
            final @NonNull AshconSnapshotService ashconSnapshotService,
            final @NonNull CacheSnapshotService cacheSnapshotService,
            final @NonNull DatabaseSnapshotService databaseSnapshotService,
            final @NonNull BukkitSnapshotService bukkitSnapshotService,
            final @NonNull PaperSnapshotService paperSnapshotService
    ) {
        super(ashconSnapshotService, cacheSnapshotService, databaseSnapshotService);
        this.audienceProvider = audienceProvider;

        this.registerSnapshotService(bukkitSnapshotService);

        if (ReflectionHelper.classExists("com.destroystokyo.paper.profile.PlayerProfile")) {
            this.registerSnapshotService(paperSnapshotService);
        }
    }

    public @NonNull PlayerUser player(final @NonNull Player player) {
        return new BukkitPlayerUser(player, this.audienceProvider.player(player.getUniqueId()));
    }

    @Override
    public @NonNull ConsoleUser console() {
        return new BukkitConsoleUser(this.audienceProvider.console());
    }

    @Override
    public @NonNull PlayerUser player(final @NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            throw new IllegalArgumentException();
        }

        return new BukkitPlayerUser(player, this.audienceProvider.player(uuid));
    }

    @Override
    public @NonNull Collection<PlayerUser> players() {
        Collection<PlayerUser> players = new ArrayList<>();

        for (final Player player : Bukkit.getOnlinePlayers()) {
            players.add(this.player(player.getUniqueId()));
        }

        return players;
    }

    @Override
    protected boolean isOnline(final @NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player != null && player.isOnline();
    }

}
