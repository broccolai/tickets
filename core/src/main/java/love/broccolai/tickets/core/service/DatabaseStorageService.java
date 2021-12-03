package love.broccolai.tickets.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.core.storage.TicketAccumulator;
import love.broccolai.tickets.core.utilities.QueriesLocator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

@Singleton
public final class DatabaseStorageService implements StorageService {

    private final QueriesLocator locator = new QueriesLocator();

    private final Jdbi jdbi;

    @Inject
    public DatabaseStorageService(final @NonNull Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public @NonNull Ticket createTicket(final @NonNull UUID creator, final @NonNull String message) {
        Instant timestamp = Instant.now();

        return this.jdbi.withHandle(handle -> {
            List<String> queries = this.locator.queries("insert-ticket");

            handle.createUpdate(queries.get(0))
                    .bind("creator", creator)
                    .bind("creationDate", timestamp)
                    .bind("message", message)
                    .execute();

            int id = handle.createQuery(queries.get(1))
                    .mapTo(Integer.class)
                    .first();

            return new Ticket(id, creator, timestamp, message, new HashSet<>());
        });
    }

}
