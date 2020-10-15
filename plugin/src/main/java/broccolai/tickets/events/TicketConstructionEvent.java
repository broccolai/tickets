package broccolai.tickets.events;

import broccolai.tickets.ticket.Message;
import broccolai.tickets.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Event representing a ticket during it's construction phase
 */
public final class TicketConstructionEvent extends ThrowableEvent {

    @NonNull
    private final PlayerSoul soul;
    @NonNull
    private final Message message;

    /**
     * Initialise the construction event
     *
     * @param soul    the ticket creator
     * @param message the tickets initial message
     */
    public TicketConstructionEvent(@NonNull final PlayerSoul soul, @NonNull final Message message) {
        super(true);
        this.soul = soul;
        this.message = message;
    }

    /**
     * Get the constructors soul
     *
     * @return Players soul
     */
    @NonNull
    public PlayerSoul getSoul() {
        return soul;
    }

    /**
     * Get the constructed tickets message
     *
     * @return Message object
     */
    @NonNull
    public Message getMessage() {
        return message;
    }

}
