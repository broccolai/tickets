package love.broccolai.tickets.api.action;

import love.broccolai.tickets.api.model.action.AssociatedTicketAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface TicketActionListener {

    void actionCreated(AssociatedTicketAction associatedAction);
}
