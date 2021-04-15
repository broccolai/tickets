package broccolai.tickets.velocity.model;

import broccolai.tickets.api.model.user.Soul;
import com.velocitypowered.api.util.GameProfile;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class VelocityOfflineSoul implements Soul {

    private final GameProfile profile;

    public VelocityOfflineSoul(final @NonNull GameProfile profile) {
        this.profile = profile;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.profile.getId();
    }

    @Override
    public @NonNull String username() {
        String username = this.profile.getName();

        if (username == null) {
            username = "UNKNOWN";
        }

        return username;
    }

}
