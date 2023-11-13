package love.broccolai.tickets.api.service;

import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.AssignAction;
import love.broccolai.tickets.api.model.action.CloseAction;
import love.broccolai.tickets.api.model.action.EditAction;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ModificationService {

    CloseAction close(Ticket ticket, UUID creator, @Nullable String message);

    EditAction edit(Ticket ticket, UUID creator, String message);

    AssignAction assign(Ticket ticket, UUID creator, UUID assignee);

}
