package broccolai.tickets.events;

import broccolai.tickets.message.Message;
import broccolai.tickets.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketConstructionEvent extends ThrowableEvent {

    private final PlayerSoul soul;
    private final Message message;

    /**
     * Initialise the construction event
     *
     * @param soul    the ticket creator
     * @param message the tickets initial message
     */
    public TicketConstructionEvent(final @NonNull PlayerSoul soul, final @NonNull Message message) {
        super(true);
        this.soul = soul;
        this.message = message;
    }

    /**
     * Get the constructors soul
     *
     * @return Players soul
     */
    public @NonNull PlayerSoul getSoul() {
        return soul;
    }

    /**
     * Get the constructed tickets message
     *
     * @return Message object
     */
    public @NonNull Message getMessage() {
        return message;
    }

}
