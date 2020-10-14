package broccolai.tickets.events;

import broccolai.tickets.user.PlayerSoul;
import org.jetbrains.annotations.NotNull;

public final class AsyncSoulJoinEvent extends BaseEvent {
    @NotNull
    final PlayerSoul soul;

    /**
     * Initialise AsyncSoulJoinEvent.
     */
    public AsyncSoulJoinEvent(@NotNull final PlayerSoul soul) {
        super(true);
        this.soul = soul;
    }

    @NotNull
    public PlayerSoul getSoul() {
        return soul;
    }
}
