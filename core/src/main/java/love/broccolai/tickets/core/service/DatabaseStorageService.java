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
import love.broccolai.tickets.core.utilities.TimeUtilities;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
public final class DatabaseStorageService implements StorageService {

    private final DelegatingActionMapper actionMapper = new DelegatingActionMapper();
    private final QueriesLocator locator = new QueriesLocator();

    private final Jdbi jdbi;

    @Inject
    public DatabaseStorageService(final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Ticket createTicket(final UUID creator, final String message) {
        Instant timestamp = TimeUtilities.nowTruncated();

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
    public void saveTicket(final Ticket ticket) {
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
                        .bind("date", action.date())
                        .bindMap(bindables)
                        .add();
            }

            batch.execute();
        });
    }

    @Override
    public Ticket selectTicket(final int id) {
        return this.selectTickets(id).get(id);
    }

    @Override
    public Map<Integer, Ticket> selectTickets(final int... ids) {
        return this.jdbi.withHandle(handle -> {
            return handle.createQuery(this.locator.query("select-tickets"))
                    .bindList("ids", Ints.asList(ids))
                    .reduceRows(new TicketAccumulator())
                    .collect(Collectors.toMap(Ticket::id, Function.identity()));
        });
    }

    @Override
    public Collection<Ticket> findTickets(
            final TicketStatus status,
            final @Nullable UUID assignee,
            final @Nullable Instant since
    ) {
        return this.jdbi.withHandle(handle -> {
            return handle.createQuery(this.locator.query("find-tickets"))
                    .bind("status", status)
                    .bind("assignee", assignee)
                    .bind("since", since)
                    .reduceRows(new TicketAccumulator())
                    .toList();
        });
    }

}
