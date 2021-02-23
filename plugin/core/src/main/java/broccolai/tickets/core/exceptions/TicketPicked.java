package broccolai.tickets.core.exceptions;

import broccolai.tickets.core.locale.Message;

public final class TicketPicked extends PureException {

    private static final long serialVersionUID = -1L;

    /**
     * Exception for when a ticket is already picked
     */
    public TicketPicked() {
        super(Message.EXCEPTION__TICKET_PICKED.use());
    }

}
