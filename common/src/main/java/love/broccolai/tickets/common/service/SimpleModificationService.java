package love.broccolai.tickets.common.service;

import com.google.inject.Inject;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.packaged.AssignAction;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.utilities.TimeUtilities;
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
        final UUID creator
    ) {
        CloseAction action = new CloseAction(TimeUtilities.nowTruncated(), creator);
        ticket.withAction(action);

        this.storageService.saveAction(ticket, action);

        return action;
    }

    @Override
    public CommentAction comment(
        final Ticket ticket,
        final UUID creator,
        final String message
    ) {
        CommentAction action = new CommentAction(TimeUtilities.nowTruncated(), creator, message);
        ticket.withAction(action);

        this.storageService.saveAction(ticket, action);

        return action;
    }

    @Override
    public AssignAction assign(
        final Ticket ticket,
        final UUID creator,
        final UUID assignee
    ) {
        AssignAction action = new AssignAction(TimeUtilities.nowTruncated(), creator, assignee);
        ticket.withAction(action);

        this.storageService.saveAction(ticket, action);

        return action;
    }

}
