package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SoulJoinEvent implements SoulEvent {

    private final PlayerSoul soul;

    /**
     * Initialise AsyncSoulJoinEvent
     */
    public SoulJoinEvent(final @NonNull PlayerSoul soul) {
        this.soul = soul;
    }

    @Override
    public @NonNull PlayerSoul soul() {
        return this.soul;
    }

}
