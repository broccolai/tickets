package love.broccolai.tickets.api.service;

import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface ModificationService {

    void close(@NonNull Ticket ticket, @NonNull UUID creator);

}
