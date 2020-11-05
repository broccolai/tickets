package broccolai.tickets.events;

import broccolai.tickets.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class AsyncSoulJoinEvent extends BaseEvent {

    private final PlayerSoul soul;

    /**
     * Initialise AsyncSoulJoinEvent
     *
     * @param soul Players soul
     */
    public AsyncSoulJoinEvent(final @NonNull PlayerSoul soul) {
        super(true);
        this.soul = soul;
    }

    /**
     * Get the joiners soul
     *
     * @return Players soul
     */
    public @NonNull PlayerSoul getSoul() {
        return soul;
    }

}
