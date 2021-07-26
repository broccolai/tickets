package broccolai.tickets.velocity.model;

import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class VelocityPlayerSoul extends Soul.Abstract implements PlayerSoul, VelocityOnlineSoul {

    private final Player player;
    private final Audience audience;

    public VelocityPlayerSoul(final @NonNull Player player, final @NonNull Audience audience) {
        this.player = player;
        this.audience = audience;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.player.getUniqueId();
    }

    @Override
    public @NonNull String username() {
        return this.player.getUsername();
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
    public CommandSource source() {
        return this.player;
    }

}
