package love.broccolai.tickets.core.service;

import com.google.inject.Inject;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.AssignAction;
import love.broccolai.tickets.api.model.action.CloseAction;
import love.broccolai.tickets.api.model.action.EditAction;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.core.utilities.TimeUtilities;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SimpleModificationService implements ModificationService {

    private final StorageService storageService;

    @Inject
    public SimpleModificationService(final StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public CloseAction close(
            final Ticket ticket,
            final UUID creator,
            final String message
    ) {
        CloseAction action = new CloseAction(TimeUtilities.nowTruncated(), creator, null);
        ticket.actions().add(action);

        this.storageService.saveTicket(ticket);

        return action;
    }

    @Override
    public EditAction edit(
            final Ticket ticket,
            final UUID creator,
            final String message
    ) {
        EditAction action = new EditAction(TimeUtilities.nowTruncated(), creator, message);

        ticket.message(message);
        ticket.actions().add(action);

        this.storageService.saveTicket(ticket);

        return action;
    }

    @Override
    public AssignAction assign(
            final Ticket ticket,
            final UUID creator,
            final UUID assignee
    ) {
        AssignAction action = new AssignAction(TimeUtilities.nowTruncated(), creator, assignee);

        ticket.assignee(assignee);
        ticket.actions().add(action);

        this.storageService.saveTicket(ticket);

        return action;
    }

}
