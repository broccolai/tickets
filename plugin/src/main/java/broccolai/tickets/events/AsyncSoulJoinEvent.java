package broccolai.tickets.events;

import broccolai.tickets.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class AsyncSoulJoinEvent extends BaseEvent {

    @NonNull
    private final PlayerSoul soul;

    /**
     * Initialise AsyncSoulJoinEvent
     *
     * @param soul Players soul
     */
    public AsyncSoulJoinEvent(@NonNull final PlayerSoul soul) {
        super(true);
        this.soul = soul;
    }

    /**
     * Get the joiners soul
     *
     * @return Players soul
     */
    @NonNull
    public PlayerSoul getSoul() {
        return soul;
    }

}
