package broccolai.tickets.events;

import broccolai.tickets.ticket.Message;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Event representing a ticket during it's construction faze.
 */
public class TicketConstructionEvent extends ThrowableEvent {
    @NotNull
    private final Player player;
    @NotNull
    private final Message message;

    /**
     * Initialise the construction event.
     *
     * @param player  the ticket creator
     * @param message the tickets initial message
     */
    public TicketConstructionEvent(@NotNull Player player, @NotNull Message message) {
        super(true);
        this.player = player;
        this.message = message;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public Message getMessage() {
        return message;
    }
}
