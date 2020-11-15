package broccolai.tickets.core.exceptions;

import broccolai.tickets.core.locale.NewMessages;

public final class TicketNotFound extends PureException {

    private static final long serialVersionUID = -1L;

    /**
     * Initialise a localised PureException
     */
    public TicketNotFound() {
        super(NewMessages.EXCEPTION__TICKET_NOT_FOUND.use());
    }

}
