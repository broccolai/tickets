package co.uk.magmo.puretickets.exceptions;

import co.uk.magmo.puretickets.locale.Messages;

public class TicketClosed extends PureException {
    public TicketClosed() {
        super(Messages.EXCEPTIONS__TICKET_CLOSED);
    }
}