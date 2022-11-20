package love.broccolai.tickets.core.service;

import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.core.storage.DelegatingActionMapper;
import love.broccolai.tickets.core.storage.TicketAccumulator;
import love.broccolai.tickets.core.utilities.QueriesLocator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;

@Singleton
public final class DatabaseStorageService implements StorageService {

    private final DelegatingActionMapper actionMapper = new DelegatingActionMapper();
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
                    .bind("date", timestamp)
                    .bind("message", message)
                    .execute();

            int id = handle.createQuery(queries.get(1))
                    .mapTo(Integer.class)
                    .first();

            return new Ticket(id, TicketStatus.OPEN, creator, timestamp, null, message, new ArrayList<>());
        });
    }

    @Override
    public void saveTicket(final @NonNull Ticket ticket) {
        this.jdbi.useHandle(handle -> {
            handle.createUpdate(this.locator.query("save-ticket"))
                    .bind("id", ticket.id())
                    .bind("status", ticket.status())
                    .bind("assignee", ticket.assignee())
                    .bind("message", ticket.message())
                    .execute();

            PreparedBatch batch = handle.prepareBatch(this.locator.query("insert-action"));

            for (final Action action : ticket.actions()) {
                String identifier = this.actionMapper.typeIdentifier(action);
                Map<String, ?> bindables = this.actionMapper.bindables(action);

                batch.bind("ticket", ticket.id())
                        .bind("type", identifier)
                        .bindMap(bindables)
                        .add();
            }

            batch.execute();
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

    @Override
    public @NonNull Collection<@NonNull Ticket> findTickets(
            @NonNull final TicketStatus status,
            @Nullable final UUID assignee
    ) {
        return this.jdbi.withHandle(handle -> {
            return handle.createQuery(this.locator.query("find-tickets"))
                    .bind("status", status)
                    .bind("assignee", assignee)
                    .reduceRows(new TicketAccumulator())
                    .toList();
        });
    }

}
