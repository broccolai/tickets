package broccolai.tickets.exceptions;

import broccolai.tickets.locale.Messages;

/**
 * Exception representing an open ticket trying to take an action.
 */
public class TicketOpen extends PureException {
    public TicketOpen() {
        super(Messages.EXCEPTIONS__TICKET_OPEN);
    }
}
