package love.broccolai.tickets.api.service;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface StorageService {

    Ticket createTicket(UUID creator, String message);

    void saveAction(Ticket ticket, Action action);

    Ticket selectTicket(int id);

    Map<Integer, Ticket> selectTickets(int... ids);

    Collection<Ticket> findTickets(
        TicketStatus status,
        @Nullable Instant since
    );
}
