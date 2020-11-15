package broccolai.tickets.core.exceptions;

import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.locale.Message;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TooManyOpenTickets extends PureException {

    private static final long serialVersionUID = -1L;

    /**
     * Exception thrown when too many tickets are open
     *
     * @param config Configuration object
     */
    public TooManyOpenTickets(final @NonNull Config config) {
        super(Message.EXCEPTION__TICKET_NOT_FOUND.use(
                Template.of("limit", config.getTicketLimitOpen().toString())
        ));
    }

}
