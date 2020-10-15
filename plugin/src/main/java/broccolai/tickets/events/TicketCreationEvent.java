package broccolai.tickets.events;

import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Event representing the creation of a ticket
 */
public final class TicketCreationEvent extends BaseEvent {

    @NonNull
    private final PlayerSoul soul;
    @NonNull
    private final Ticket ticket;

    /**
     * Initialise the creation event
     *
     * @param soul   the ticket creator
     * @param ticket the constructed ticket
     */
    public TicketCreationEvent(@NonNull final PlayerSoul soul, @NonNull final Ticket ticket) {
        super(true);
        this.soul = soul;
        this.ticket = ticket;
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
     * Get the created ticket
     *
     * @return Ticket object
     */
    @NonNull
    public Ticket getTicket() {
        return ticket;
    }

}
