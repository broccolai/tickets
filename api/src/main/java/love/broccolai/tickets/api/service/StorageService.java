package love.broccolai.tickets.api.service;

import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface StorageService {

    @NonNull Ticket createTicket(@NonNull UUID creator, @NonNull String message);

    void saveTicket(@NonNull Ticket ticket);

    @NonNull Ticket selectTicket(int id);

    @NonNull Map<@NonNull Integer, @NonNull Ticket> selectTickets(int... ids);
}
