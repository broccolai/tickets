package broccolai.tickets.velocity.service;

import broccolai.tickets.api.model.user.ConsoleUser;
import broccolai.tickets.api.model.user.PlayerUser;
import broccolai.tickets.core.service.user.SimpleUserService;
import broccolai.tickets.core.service.user.snapshot.AshconSnapshotService;
import broccolai.tickets.core.service.user.snapshot.CacheSnapshotService;
import broccolai.tickets.core.service.user.snapshot.DatabaseSnapshotService;
import broccolai.tickets.velocity.model.VelocityConsoleUser;
import broccolai.tickets.velocity.model.VelocityPlayerUser;
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

    public @NonNull PlayerUser player(final @NonNull Player player) {
        return new VelocityPlayerUser(player, this.audienceProvider.player(player.getUniqueId()));
    }

    @Override
    public @NonNull ConsoleUser console() {
        return new VelocityConsoleUser(this.server.getConsoleCommandSource());
    }

    @Override
    public @NonNull PlayerUser player(final @NonNull UUID uuid) {
        Player player = this.server.getPlayer(uuid)
                .orElseThrow(IllegalArgumentException::new);

        return new VelocityPlayerUser(player, this.audienceProvider.player(uuid));
    }

    @Override
    public @NonNull Collection<PlayerUser> players() {
        Collection<PlayerUser> players = new ArrayList<>();

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
