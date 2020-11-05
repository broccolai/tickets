package broccolai.tickets.exceptions;

import broccolai.tickets.locale.Messages;

public final class TicketOpen extends PureException {

    /**
     * Exception thrown when the ticket is already open
     */
    public TicketOpen() {
        super(Messages.EXCEPTIONS__TICKET_OPEN);
    }

}
