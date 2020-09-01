package broccolai.tickets.exceptions;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.locale.Messages;

/**
 * Exception representing a player trying to open another ticket when they're at the limit.
 */
public class TooManyOpenTickets extends PureException {
    public TooManyOpenTickets(Config config) {
        super(Messages.EXCEPTIONS__TOO_MANY_OPEN_TICKETS, "%limit%", config.LIMIT__OPEN_TICKETS.toString());
    }
}