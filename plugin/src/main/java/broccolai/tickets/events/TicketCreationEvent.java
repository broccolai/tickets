package broccolai.tickets.events;

import broccolai.tickets.ticket.Ticket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Event representing the creation of a ticket.
 */
public class TicketCreationEvent extends BaseEvent {
    @NotNull
    private final Player player;
    @NotNull
    private final Ticket ticket;

    /**
     * Initialise the creation event.
     *
     * @param player the ticket creator
     * @param ticket the constructed ticket
     */
    public TicketCreationEvent(@NotNull Player player, @NotNull Ticket ticket) {
        super(true);
        this.player = player;
        this.ticket = ticket;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public Ticket getTicket() {
        return ticket;
    }
}
