package love.broccolai.tickets.core.service;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.CloseAction;
import love.broccolai.tickets.api.service.ModificationService;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SimpleModificationService implements ModificationService {

    @Override
    public void close(final @NonNull Ticket ticket, final @NonNull UUID creator) {
        Action action = new CloseAction(creator, Instant.now());
        ticket.actions().add(action);
    }

}
