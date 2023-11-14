package love.broccolai.tickets.common.service;

import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.model.SimpleTicket;
import love.broccolai.tickets.common.storage.DelegatingActionMapper;
import love.broccolai.tickets.common.storage.TicketAccumulator;
import love.broccolai.tickets.common.storage.actions.OpenActionMapper;
import love.broccolai.tickets.common.utilities.QueriesLocator;
import love.broccolai.tickets.common.utilities.TimeUtilities;
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
                .execute();

            int id = handle.createQuery(queries.get(1))
                .mapTo(Integer.class)
                .first();

            Ticket ticket = new SimpleTicket(id, creator, timestamp, new TreeSet<>(Action.SORTER));
            OpenAction action = new OpenAction(timestamp, creator, message);

            ticket.actions().add(action);

            //todo: extract
            handle.createUpdate(this.locator.query("insert-action"))
                .bind("ticket", ticket.id())
                .bind("type", "OPEN")
                .bind("date", action.date())
                .bind("creator", action.creator())
                .bindMap(OpenActionMapper.INSTANCE.bindables(action))
                .execute();

            return ticket;
        });
    }

    // todo: improve this now that tickets are immutable?
    @Override
    public void saveTicket(final Ticket ticket) {
        this.jdbi.useHandle(handle -> {
            PreparedBatch batch = handle.prepareBatch(this.locator.query("insert-action"));

            for (final Action action : ticket.actions()) {
                String identifier = this.actionMapper.typeIdentifier(action);
                Map<String, ?> bindables = this.actionMapper.bindables(action);

                batch.bind("ticket", ticket.id())
                    .bind("type", identifier)
                    .bind("date", action.date())
                    .bind("creator", action.creator())
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
        final @Nullable Instant since
    ) {
        List<Ticket> filteredTickets = this.jdbi.withHandle(handle -> {
            return handle.createQuery(this.locator.query("find-tickets"))
                .bind("since", since)
                .reduceRows(new TicketAccumulator())
                .collect(Collectors.toList());
        });

        filteredTickets.removeIf(ticket -> ticket.status() != status);

        return filteredTickets;
    }

}
