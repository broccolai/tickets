package broccolai.tickets.events;

import broccolai.tickets.ticket.Ticket;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

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
