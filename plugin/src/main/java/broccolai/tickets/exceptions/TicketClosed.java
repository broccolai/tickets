package broccolai.tickets.exceptions;

import broccolai.tickets.locale.Messages;

/**
 * Exception representing a ticket that has already been closed trying to take an action.
 */
public class TicketClosed extends PureException {
    public TicketClosed() {
        super(Messages.EXCEPTIONS__TICKET_CLOSED);
    }
}