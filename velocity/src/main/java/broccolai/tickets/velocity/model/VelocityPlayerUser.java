package broccolai.tickets.velocity.model;

import broccolai.tickets.api.model.user.PlayerUser;
import broccolai.tickets.api.model.user.User;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class VelocityPlayerUser extends User.Abstract implements PlayerUser, VelocityOnlineUser {

    private final Player player;
    private final Audience audience;

    public VelocityPlayerUser(final @NonNull Player player, final @NonNull Audience audience) {
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
