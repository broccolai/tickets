package love.broccolai.tickets.common.service.storage;

import love.broccolai.tickets.api.model.action.TicketAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record StoredTicketAction(
    int actionId,
    int ticketId,
    TicketAction action
) {
}
