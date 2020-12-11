package broccolai.tickets.core.events.api;

import broccolai.tickets.core.events.Event;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketCreationEvent implements Event {

    private final PlayerSoul<?, ?> soul;
    private final Ticket ticket;

    /**
     * Initialise the creation event
     *
     * @param soul   Ticket creator
     * @param ticket Constructed ticket
     */
    public TicketCreationEvent(final @NonNull PlayerSoul<?, ?> soul, final @NonNull Ticket ticket) {
        this.soul = soul;
        this.ticket = ticket;
    }

    /**
     * Get the constructors soul
     *
     * @return Players soul
     */
    public @NonNull PlayerSoul<?, ?> getSoul() {
        return this.soul;
    }

    /**
     * Get the created ticket
     *
     * @return Ticket object
     */
    public @NonNull Ticket getTicket() {
        return this.ticket;
    }

}
