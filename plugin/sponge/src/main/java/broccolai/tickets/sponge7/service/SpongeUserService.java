package broccolai.tickets.sponge7.service;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.core.service.user.SimpleUserService;
import broccolai.tickets.sponge7.model.SpongeConsoleSoul;
import broccolai.tickets.sponge7.model.SpongeOfflineSoul;
import broccolai.tickets.sponge7.model.SpongePlayerSoul;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.platform.AudienceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Singleton
public final class SpongeUserService extends SimpleUserService {

    private final AudienceProvider audienceProvider;
    private final UserStorageService userStorageService;
    private final GameProfileManager profileManager;

    @Inject
    public SpongeUserService(final @NonNull AudienceProvider audienceProvider) {
        this.audienceProvider = audienceProvider;
        this.userStorageService = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
        this.profileManager = Sponge.getServer().getGameProfileManager();
    }

    public @NonNull PlayerSoul player(final @NonNull Player player) {
        return new SpongePlayerSoul(player, this.audienceProvider.player(player.getUniqueId()));
    }

    @Override
    public @NonNull ConsoleSoul console() {
        return new SpongeConsoleSoul(this.audienceProvider.console());
    }

    @Override
    public @NonNull PlayerSoul player(final @NonNull UUID uuid) {
        Player player = Sponge.getServer().getPlayer(uuid).orElseThrow(IllegalArgumentException::new);

        return new SpongePlayerSoul(player, this.audienceProvider.player(uuid));
    }

    @Override
    public @NonNull Soul offlinePlayer(@NonNull final UUID uuid) {
        try {
            GameProfile profile = this.profileManager.get(uuid).get();
            return new SpongeOfflineSoul(profile);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    public @NonNull Collection<PlayerSoul> players() {
        Collection<PlayerSoul> players = new ArrayList<>();

        for (final Player player : Sponge.getServer().getOnlinePlayers()) {
            players.add(this.player(player.getUniqueId()));
        }

        return players;
    }

    @Override
    public @NonNull String name(@NonNull final UUID uuid) {
        try {
            return this.profileManager.get(uuid).get().getName().orElse("UNKNOWN");
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NonNull UUID uuidFromName(@NonNull final String name) {
        try {
            return this.profileManager.get(name).get().getUniqueId();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    protected boolean isOnline(@NonNull final UUID uuid) {
        return this.userStorageService.get(uuid).map(User::isOnline).orElse(false);
    }

}
