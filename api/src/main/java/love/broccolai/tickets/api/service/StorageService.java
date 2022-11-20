package love.broccolai.tickets.api.service;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface StorageService {

    @NonNull Ticket createTicket(@NonNull UUID creator, @NonNull String message);

    //todo(josh): rework into transaction based system.
    void saveTicket(@NonNull Ticket ticket);

    @NonNull Ticket selectTicket(int id);

    @NonNull Map<@NonNull Integer, @NonNull Ticket> selectTickets(int... ids);

    @NonNull Collection<@NonNull Ticket> findTickets(
            @NonNull TicketStatus status,
            @Nullable UUID assignee,
            @Nullable Instant since
    );
}
