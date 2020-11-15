package broccolai.tickets.core.exceptions;

import broccolai.tickets.core.locale.Message;

public final class TicketNotFound extends PureException {

    private static final long serialVersionUID = -1L;

    /**
     * Initialise a localised PureException
     */
    public TicketNotFound() {
        super(Message.EXCEPTION__TICKET_NOT_FOUND.use());
    }

}
