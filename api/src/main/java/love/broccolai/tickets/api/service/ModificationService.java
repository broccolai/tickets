package love.broccolai.tickets.api.service;

import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.AssignAction;
import love.broccolai.tickets.api.model.action.CloseAction;
import love.broccolai.tickets.api.model.action.EditAction;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ModificationService {

    @NonNull CloseAction close(@NonNull Ticket ticket, @NonNull UUID creator, @Nullable String message);

    @NonNull EditAction edit(@NonNull Ticket ticket, @NonNull UUID creator, @NonNull String message);

    @NonNull AssignAction assign(@NonNull Ticket ticket, @NonNull UUID creator, @NonNull UUID assignee);

}
