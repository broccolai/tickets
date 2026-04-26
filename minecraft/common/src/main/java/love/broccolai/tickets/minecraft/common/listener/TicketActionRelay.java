package love.broccolai.tickets.minecraft.common.listener;

import com.google.inject.Inject;
import love.broccolai.tickets.api.action.TicketActionListener;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.AssociatedTicketAction;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.minecraft.common.service.TicketNotifier;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NullMarked
public final class TicketActionRelay implements TicketActionListener {

    private static final Logger logger = LoggerFactory.getLogger(TicketActionRelay.class);

    private final StorageService storageService;
    private final TicketNotifier ticketNotifier;

    @Inject
    public TicketActionRelay(
        final StorageService storageService,
        final TicketNotifier ticketNotifier
    ) {
        this.storageService = storageService;
        this.ticketNotifier = ticketNotifier;
    }

    @Override
    public void actionCreated(final AssociatedTicketAction associatedAction) {
        Ticket ticket = this.storageService.selectTicket(associatedAction.ticketId()).orElseThrow();
        TicketAction ticketAction = associatedAction.action();

        logger.info("ticket {} has received action {}", ticket.id(), ticketAction.getClass().getSimpleName());
        this.ticketNotifier.notify(associatedAction, ticket);
    }
}
