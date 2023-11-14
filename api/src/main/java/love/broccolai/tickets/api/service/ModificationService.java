package love.broccolai.tickets.api.service;

import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.packaged.AssignAction;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ModificationService {

    CloseAction close(Ticket ticket, UUID creator);

    CommentAction comment(Ticket ticket, UUID creator, String message);

    AssignAction assign(Ticket ticket, UUID creator, UUID assignee);

}
