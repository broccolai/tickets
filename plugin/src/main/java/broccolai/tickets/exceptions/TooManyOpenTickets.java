package broccolai.tickets.exceptions;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.locale.Messages;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Exception representing a player trying to open another ticket when they're at the limit
 */
public final class TooManyOpenTickets extends PureException {

    /**
     * Exception thrown when too many tickets are open
     *
     * @param config Configuration object
     */
    public TooManyOpenTickets(@NonNull final Config config) {
        super(Messages.EXCEPTIONS__TOO_MANY_OPEN_TICKETS, "limit", config.getTicketLimitOpen().toString());
    }

}
