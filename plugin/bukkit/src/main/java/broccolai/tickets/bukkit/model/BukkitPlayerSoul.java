package broccolai.tickets.bukkit.model;

import broccolai.tickets.api.model.user.PlayerSoul;
import java.util.UUID;
import broccolai.tickets.api.model.user.Soul;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BukkitPlayerSoul extends Soul.Abstract implements PlayerSoul, BukkitOnlineSoul {

    private final Player player;
    private final Audience audience;

    public BukkitPlayerSoul(final @NonNull Player player, final @NonNull Audience audience) {
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
    public boolean permission(final @NonNull String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public @NonNull Audience audience() {
        return this.audience;
    }

    @Override
    public Player sender() {
        return this.player;
    }

}
