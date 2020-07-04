package co.uk.magmo.puretickets.exceptions;

import co.uk.magmo.puretickets.configuration.Config;
import co.uk.magmo.puretickets.locale.Messages;

public class TooManyOpenTickets extends PureException {
    public TooManyOpenTickets(Config config) {
        super(Messages.EXCEPTIONS__TOO_MANY_OPEN_TICKETS, "%limit%", config.LIMIT__OPEN_TICKETS.toString());
    }
}