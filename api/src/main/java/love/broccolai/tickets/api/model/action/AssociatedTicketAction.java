package love.broccolai.tickets.api.model.action;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record AssociatedTicketAction(
    int ticketId,
    TicketAction action
) {
}
