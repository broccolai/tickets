package broccolai.tickets.core.exceptions;

import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.locale.Messages;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TooManyOpenTickets extends PureException {

    private static final long serialVersionUID = -1L;

    /**
     * Exception thrown when too many tickets are open
     *
     * @param config Configuration object
     */
    public TooManyOpenTickets(final @NonNull Config config) {
        super(Messages.EXCEPTIONS__TOO_MANY_OPEN_TICKETS, "limit", config.getTicketLimitOpen().toString());
    }

}
