package broccolai.tickets.events.api;

import broccolai.tickets.events.Event;
import broccolai.tickets.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SoulJoinEvent implements Event {

    private final PlayerSoul soul;

    /**
     * Initialise AsyncSoulJoinEvent
     *
     * @param soul Players soul
     */
    public SoulJoinEvent(final @NonNull PlayerSoul soul) {
        this.soul = soul;
    }

    /**
     * Get the joiners soul
     *
     * @return Players soul
     */
    public @NonNull PlayerSoul getSoul() {
        return this.soul;
    }

}
