package broccolai.tickets.core.exceptions;

import broccolai.tickets.core.locale.Message;

public final class TicketOpen extends PureException {

    private static final long serialVersionUID = -1L;

    /**
     * Exception thrown when the ticket is already open
     */
    public TicketOpen() {
        super(Message.EXCEPTION__TICKET_OPEN.use());
    }

}
