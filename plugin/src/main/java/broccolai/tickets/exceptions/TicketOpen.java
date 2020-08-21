package broccolai.tickets.exceptions;

import broccolai.tickets.locale.Messages;

public class TicketOpen extends PureException {
    public TicketOpen() {
        super(Messages.EXCEPTIONS__TICKET_OPEN);
    }
}
