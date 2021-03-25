package broccolai.tickets.sponge7.model;

import broccolai.tickets.api.model.user.Soul;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.profile.GameProfile;

import java.util.UUID;

public final class SpongeOfflineSoul implements Soul {

    private final GameProfile gameProfile;

    public SpongeOfflineSoul(final @NonNull GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.gameProfile.getUniqueId();
    }

    @Override
    public @NonNull String username() {
        return this.gameProfile.getName().orElse("UNKNOWN");
    }

}
