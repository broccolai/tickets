package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.user.PlayerUser;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SoulJoinEvent implements SoulEvent {

    private final PlayerUser soul;

    public SoulJoinEvent(final @NonNull PlayerUser soul) {
        this.soul = soul;
    }

    @Override
    public @NonNull PlayerUser soul() {
        return this.soul;
    }

}
