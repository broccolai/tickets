package love.broccolai.tickets.core.service;

import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
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

            return new Ticket(id, creator, timestamp, null, message, new HashSet<>());
        });
    }

    @Override
    public void saveTicket(final @NonNull Ticket ticket) {
        this.jdbi.useHandle(handle -> {
            handle.createUpdate(this.locator.query("save-ticket"))
                    .bind("id", ticket.id())
                    .bind("assignee", ticket.assignee())
                    .bind("message", ticket.message())
                    .execute();
        });
    }

    @Override
    public @NonNull Ticket selectTicket(final int id) {
        return this.selectTickets(id).get(id);
    }

    @Override
    public @NonNull Map<@NonNull Integer, @NonNull Ticket> selectTickets(final int... ids) {
        return this.jdbi.withHandle(handle -> {
            return handle.createQuery(this.locator.query("select-tickets"))
                    .bindList("ids", Ints.asList(ids))
                    .reduceRows(new TicketAccumulator())
                    .collect(Collectors.toMap(Ticket::id, Function.identity()));
        });
    }

}
