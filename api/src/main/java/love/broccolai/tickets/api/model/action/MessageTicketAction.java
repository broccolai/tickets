package love.broccolai.tickets.api.model.action;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MessageTicketAction extends TicketAction {

    String message();
}
