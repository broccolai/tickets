package broccolai.tickets.events;

import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketCreationEvent extends BaseEvent {

    private final PlayerSoul soul;
    private final Ticket ticket;

    /**
     * Initialise the creation event
     *
     * @param soul   the ticket creator
     * @param ticket the constructed ticket
     */
    public TicketCreationEvent(final @NonNull PlayerSoul soul, final @NonNull Ticket ticket) {
        super(true);
        this.soul = soul;
        this.ticket = ticket;
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
     * Get the created ticket
     *
     * @return Ticket object
     */
    public @NonNull Ticket getTicket() {
        return ticket;
    }

}
