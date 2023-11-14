package love.broccolai.tickets.api.model.action;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MessageAction extends Action {

    String message();

}
