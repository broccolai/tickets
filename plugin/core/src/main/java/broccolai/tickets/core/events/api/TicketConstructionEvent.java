package broccolai.tickets.core.events.api;

import broccolai.tickets.core.events.ExceptionEvent;
import broccolai.tickets.core.message.Message;

import broccolai.tickets.core.model.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketConstructionEvent extends ExceptionEvent {

    private final PlayerSoul soul;
    private final Message message;

    /**
     * Initialise the construction event
     *
     * @param soul    the ticket creators unique id
     * @param message the tickets initial message
     */
    public TicketConstructionEvent(final @NonNull PlayerSoul soul, final @NonNull Message message) {
        this.soul = soul;
        this.message = message;
    }

    /**
     * Get the constructors soul
     *
     * @return Players soul
     */
    public @NonNull PlayerSoul soul() {
        return this.soul;
    }

    /**
     * Get the constructed tickets message
     *
     * @return Message object
     */
    public @NonNull Message getMessage() {
        return this.message;
    }

}
