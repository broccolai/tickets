package broccolai.tickets.core.ticket;

import java.util.EnumMap;

public final class TicketStats extends EnumMap<TicketStatus, Integer> {

    private static final long serialVersionUID = -1L;

    /**
     * Construct a Ticket Stats instance
     */
    public TicketStats() {
        super(TicketStatus.class);
    }

}
