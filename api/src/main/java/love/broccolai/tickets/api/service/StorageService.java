package love.broccolai.tickets.api.service;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface StorageService {

    Ticket createTicket(UUID creator, String message);

    //todo(josh): rework into transaction based system.
    void saveTicket(Ticket ticket);

    Ticket selectTicket(int id);

    Map<Integer, Ticket> selectTickets(int... ids);

    Collection<Ticket> findTickets(
        TicketStatus status,
        @Nullable UUID assignee,
        @Nullable Instant since
    );
}
