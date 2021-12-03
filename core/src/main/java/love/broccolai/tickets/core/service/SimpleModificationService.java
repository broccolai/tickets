package love.broccolai.tickets.core.service;

import com.google.inject.Inject;
import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.AssignAction;
import love.broccolai.tickets.api.model.action.CloseAction;
import love.broccolai.tickets.api.model.action.EditAction;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SimpleModificationService implements ModificationService {

    private final StorageService storageService;

    @Inject
    public SimpleModificationService(final @NonNull StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public @NonNull CloseAction close(
            final @NonNull Ticket ticket,
            final @NonNull UUID creator,
            final @Nullable String message
    ) {
        CloseAction action = new CloseAction(Instant.now(), creator, null);
        ticket.actions().add(action);

        return action;
    }

    @Override
    public @NonNull EditAction edit(
            final @NonNull Ticket ticket,
            final @NonNull UUID creator,
            final @NonNull String message
    ) {
        EditAction action = new EditAction(Instant.now(), creator, message);

        ticket.message(message);
        ticket.actions().add(action);

        this.storageService.saveTicket(ticket);

        return action;
    }

    @Override
    public @NonNull AssignAction assign(
            final @NonNull Ticket ticket,
            final @NonNull UUID creator,
            final @NonNull UUID assignee
    ) {
        AssignAction action = new AssignAction(Instant.now(), creator, assignee);

        ticket.assignee(assignee);
        ticket.actions().add(action);

        this.storageService.saveTicket(ticket);

        return action;
    }

}
