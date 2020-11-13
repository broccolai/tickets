package broccolai.tickets.core.exceptions;

import broccolai.tickets.core.locale.Messages;

public final class TicketOpen extends PureException {

    private static final long serialVersionUID = -1L;

    /**
     * Exception thrown when the ticket is already open
     */
    public TicketOpen() {
        super(Messages.EXCEPTIONS__TICKET_OPEN);
    }

}
