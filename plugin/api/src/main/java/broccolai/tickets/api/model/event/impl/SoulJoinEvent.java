package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public final class SoulJoinEvent implements SoulEvent {

    private final PlayerSoul soul;

    public SoulJoinEvent(final @NonNull PlayerSoul soul) {
        this.soul = soul;
    }

    @Override
    public @NonNull PlayerSoul soul() {
        return this.soul;
    }

}
