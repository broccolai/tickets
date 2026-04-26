package love.broccolai.tickets.minecraft.common.service;

import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.AssociatedTicketAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TicketNotifier {

    void notify(AssociatedTicketAction associatedAction, Ticket ticket);
}
