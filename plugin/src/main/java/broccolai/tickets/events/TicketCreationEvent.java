package broccolai.tickets.events;

import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.user.PlayerSoul;
import org.jetbrains.annotations.NotNull;

/**
 * Event representing the creation of a ticket.
 */
public final class TicketCreationEvent extends BaseEvent {
    @NotNull
    private final PlayerSoul soul;
    @NotNull
    private final Ticket ticket;

    /**
     * Initialise the creation event.
     *
     * @param soul   the ticket creator
     * @param ticket the constructed ticket
     */
    public TicketCreationEvent(@NotNull PlayerSoul soul, @NotNull Ticket ticket) {
        super(true);
        this.soul = soul;
        this.ticket = ticket;
    }

    @NotNull
    public PlayerSoul getSoul() {
        return soul;
    }

    @NotNull
    public Ticket getTicket() {
        return ticket;
    }
}
