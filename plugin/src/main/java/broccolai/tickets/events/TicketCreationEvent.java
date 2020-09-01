package broccolai.tickets.events;

import broccolai.tickets.ticket.Ticket;
import org.bukkit.entity.Player;

public class TicketCreationEvent extends BaseEvent {
    private final Player player;
    private final Ticket ticket;

    public TicketCreationEvent(Player player, Ticket ticket) {
        super(true);
        this.player = player;
        this.ticket = ticket;
    }

    public Player getPlayer() {
        return player;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
