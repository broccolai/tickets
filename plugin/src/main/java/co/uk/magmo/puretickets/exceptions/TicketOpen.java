package co.uk.magmo.puretickets.exceptions;

import co.uk.magmo.puretickets.locale.Messages;

public class TicketOpen extends PureException {
    public TicketOpen() {
        super(Messages.EXCEPTIONS__TICKET_OPEN);
    }
}
