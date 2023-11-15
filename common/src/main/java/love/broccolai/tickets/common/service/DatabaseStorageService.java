package love.broccolai.tickets.common.service;

import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.model.SimpleTicket;
import love.broccolai.tickets.common.serialization.jdbi.ActionMapper;
import love.broccolai.tickets.common.serialization.jdbi.TicketAccumulator;
import love.broccolai.tickets.common.utilities.QueriesLocator;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.qualifier.QualifiedType;
import org.jdbi.v3.core.statement.Update;
import org.jdbi.v3.json.Json;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
public final class DatabaseStorageService implements StorageService {

    private final QueriesLocator locator = new QueriesLocator();

    private final Jdbi jdbi;
    private final ActionMapper actionMapper;

    @Inject
    public DatabaseStorageService(final Jdbi jdbi, ActionMapper actionMapper) {
        this.jdbi = jdbi;
        this.actionMapper = actionMapper;
    }

    @Override
    public Ticket createTicket(final UUID creator, final String message) {
        Instant timestamp = TimeUtilities.nowTruncated();
        OpenAction action = new OpenAction(timestamp, creator, message);
        QualifiedType<OpenAction> type = QualifiedType.of(OpenAction.class).with(Json.class);

        return this.jdbi.inTransaction(handle -> {
            List<String> queries = this.locator.queries("insert-ticket");

            int id = handle.createUpdate(queries.get(0))
                .bind("creator", creator)
                .bind("date", timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(Integer.class)
                .one();

            handle.createUpdate(queries.get(1))
                .bindByType("data", action, type)
                .execute();

            Ticket ticket = new SimpleTicket(id, creator, timestamp, new LinkedHashSet<>());
            ticket.withAction(action);

            return ticket;
        });
    }

    @Override
    public void saveAction(final Ticket ticket, final Action action) {
        this.jdbi.useHandle(handle -> {
            Update statement = handle.createUpdate(this.locator.query("insert-action"))
                .bind("ticket", ticket.id());

            this.actionMapper.bindToStatement(statement, action);

            statement.execute();
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
