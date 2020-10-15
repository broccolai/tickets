package broccolai.tickets.events;

import broccolai.tickets.ticket.Message;
import broccolai.tickets.user.PlayerSoul;
import org.jetbrains.annotations.NotNull;

/**
 * Event representing a ticket during it's construction phase
 */
public final class TicketConstructionEvent extends ThrowableEvent {

    @NotNull
    private final PlayerSoul soul;
    @NotNull
    private final Message message;

    /**
     * Initialise the construction event
     *
     * @param soul    the ticket creator
     * @param message the tickets initial message
     */
    public TicketConstructionEvent(@NotNull final PlayerSoul soul, @NotNull final Message message) {
        super(true);
        this.soul = soul;
        this.message = message;
    }

    /**
     * Get the constructors soul
     *
     * @return Players soul
     */
    @NotNull
    public PlayerSoul getSoul() {
        return soul;
    }

    /**
     * Get the constructed tickets message
     *
     * @return Message object
     */
    @NotNull
    public Message getMessage() {
        return message;
    }

}
