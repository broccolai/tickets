package broccolai.tickets.velocity.service;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.velocity.model.VelocityConsoleSoul;
import broccolai.tickets.velocity.model.VelocityPlayerSoul;
import broccolai.tickets.core.service.user.SimpleUserService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.ArrayList;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.platform.AudienceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Collection;
import java.util.UUID;

@Singleton
public final class VelocityUserService extends SimpleUserService {

    private final ProxyServer server;
    private final AudienceProvider audienceProvider;

    @Inject
    public VelocityUserService(
            final @NonNull Injector injector,
            final @NonNull ProxyServer server,
            final @NonNull AudienceProvider audienceProvider
    ) {
        super(injector);
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
