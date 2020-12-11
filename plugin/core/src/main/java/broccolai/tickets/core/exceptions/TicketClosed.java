package broccolai.tickets.core.exceptions;

import broccolai.tickets.core.locale.Message;

public final class TicketClosed extends PureException {

    private static final long serialVersionUID = -1L;

    /**
     * Exception for when a ticket is already closed
     */
    public TicketClosed() {
        super(Message.EXCEPTION__TICKET_CLOSED.use());
    }

}
