package broccolai.tickets.events.api;

import broccolai.tickets.events.Event;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketCreationEvent implements Event {

    private final PlayerSoul soul;
    private final Ticket ticket;

    /**
     * Initialise the creation event
     *
     * @param soul   Ticket creator
     * @param ticket Constructed ticket
     */
    public TicketCreationEvent(final @NonNull PlayerSoul soul, final @NonNull Ticket ticket) {
        this.soul = soul;
        this.ticket = ticket;
    }

    /**
     * Get the constructors soul
     *
     * @return Players soul
     */
    public @NonNull PlayerSoul getSoul() {
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
