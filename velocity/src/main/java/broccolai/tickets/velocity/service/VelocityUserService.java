package broccolai.tickets.velocity.service;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.core.service.user.SimpleUserService;
import broccolai.tickets.core.service.user.snapshot.AshconSnapshotService;
import broccolai.tickets.core.service.user.snapshot.CacheSnapshotService;
import broccolai.tickets.core.service.user.snapshot.DatabaseSnapshotService;
import broccolai.tickets.velocity.model.VelocityConsoleSoul;
import broccolai.tickets.velocity.model.VelocityPlayerSoul;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import net.kyori.adventure.platform.AudienceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class VelocityUserService extends SimpleUserService {

    private final ProxyServer server;
    private final AudienceProvider audienceProvider;

    @Inject
    public VelocityUserService(
            final @NonNull ProxyServer server,
            final @NonNull AudienceProvider audienceProvider,
            final @NonNull AshconSnapshotService ashconSnapshotService,
            final @NonNull CacheSnapshotService cacheSnapshotService,
            final @NonNull DatabaseSnapshotService databaseSnapshotService
    ) {
        super(ashconSnapshotService, cacheSnapshotService, databaseSnapshotService);
        this.server = server;
        this.audienceProvider = audienceProvider;
    }

    public @NonNull PlayerSoul player(final @NonNull Player player) {
        return new VelocityPlayerSoul(player, this.audienceProvider.player(player.getUniqueId()));
    }

    @Override
    public @NonNull ConsoleSoul console() {
        return new VelocityConsoleSoul(this.server.getConsoleCommandSource());
    }

    @Override
    public @NonNull PlayerSoul player(final @NonNull UUID uuid) {
        Player player = this.server.getPlayer(uuid)
                .orElseThrow(IllegalArgumentException::new);

        return new VelocityPlayerSoul(player, this.audienceProvider.player(uuid));
    }

    @Override
    public @NonNull Collection<PlayerSoul> players() {
        Collection<PlayerSoul> players = new ArrayList<>();

        for (final Player player : this.server.getAllPlayers()) {
            players.add(this.player(player.getUniqueId()));
        }

        return players;
    }

    @Override
    protected boolean isOnline(final @NonNull UUID uuid) {
        return this.server.getPlayer(uuid).isPresent();
    }

}
