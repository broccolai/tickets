package broccolai.tickets.events;

import broccolai.tickets.user.PlayerSoul;
import org.jetbrains.annotations.NotNull;

public final class AsyncSoulJoinEvent extends BaseEvent {

    @NotNull
    private final PlayerSoul soul;

    /**
     * Initialise AsyncSoulJoinEvent
     *
     * @param soul Players soul
     */
    public AsyncSoulJoinEvent(@NotNull final PlayerSoul soul) {
        super(true);
        this.soul = soul;
    }

    /**
     * Get the joiners soul
     *
     * @return Players soul
     */
    @NotNull
    public PlayerSoul getSoul() {
        return soul;
    }

}
