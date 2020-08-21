package broccolai.tickets.exceptions;

import broccolai.tickets.locale.Messages;

public class TicketClosed extends PureException {
    public TicketClosed() {
        super(Messages.EXCEPTIONS__TICKET_CLOSED);
    }
}