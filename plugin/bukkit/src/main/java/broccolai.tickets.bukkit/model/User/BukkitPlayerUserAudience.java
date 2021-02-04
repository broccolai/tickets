package broccolai.tickets.bukkit.model.User;

import broccolai.tickets.core.model.user.PlayerUserAudience;

import java.util.UUID;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BukkitPlayerUserAudience implements PlayerUserAudience {

    private final Player player;
    private final Audience audience;

    public BukkitPlayerUserAudience(final @NonNull Player player, final @NonNull Audience audience) {
        this.player = player;
        this.audience = audience;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.player.getUniqueId();
    }

    @Override
    public @NonNull String username() {
        return this.player.getName();
    }

    @Override
    public @NonNull Audience audience() {
        return this.audience;
    }

}
