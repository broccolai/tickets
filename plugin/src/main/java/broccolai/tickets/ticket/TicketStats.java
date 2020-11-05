package broccolai.tickets.ticket;

import java.util.EnumMap;

public final class TicketStats extends EnumMap<TicketStatus, Integer> {

    /**
     * Construct a Ticket Stats instance
     */
    public TicketStats() {
        super(TicketStatus.class);
    }

}
