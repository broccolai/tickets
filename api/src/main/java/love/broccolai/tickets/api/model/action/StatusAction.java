package love.broccolai.tickets.api.model.action;

import love.broccolai.tickets.api.model.TicketStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface StatusAction extends Action {

    TicketStatus status();

}
