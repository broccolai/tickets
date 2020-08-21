package broccolai.tickets.exceptions;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.locale.Messages;

public class TooManyOpenTickets extends PureException {
    public TooManyOpenTickets(Config config) {
        super(Messages.EXCEPTIONS__TOO_MANY_OPEN_TICKETS, "%limit%", config.LIMIT__OPEN_TICKETS.toString());
    }
}