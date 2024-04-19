package love.broccolai.tickets.common.service;

import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.TicketType;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.AssociatedAction;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import love.broccolai.tickets.common.model.SimpleTicket;
import love.broccolai.tickets.common.serialization.jdbi.ActionMapper;
import love.broccolai.tickets.common.serialization.jdbi.TicketAccumulator;
import love.broccolai.tickets.common.utilities.QueriesLocator;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.qualifier.QualifiedType;
import org.jdbi.v3.core.statement.Update;
import org.jdbi.v3.json.Json;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@NullMarked
public final class DatabaseStorageService implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseStorageService.class);

    private final Jdbi jdbi;
    private final ActionMapper actionMapper;
    private final QueriesLocator locator;

    @Inject
    public DatabaseStorageService(
        final Jdbi jdbi,
        final ActionMapper actionMapper,
        final DatabaseConfiguration configuration
    ) {
        this.jdbi = jdbi;
        this.actionMapper = actionMapper;
        this.locator = new QueriesLocator(configuration.type);
    }

    @Override
    public void addNotificationListener(final PGNotificationListener listener) {
        this.jdbi.useHandle(handle -> {
            handle.execute("LISTEN action_channel");

            PGConnection connection = this.connectionFromHandle(handle);
            logger.trace("Adding notification listener: {}", listener.getClass().getSimpleName());
            connection.addNotificationListener(listener);
        });
    }

    private PGConnection connectionFromHandle(final Handle handle) {
        try (Connection conn = handle.getConnection()) {
            return conn.unwrap(PGConnection.class);
        } catch (SQLException e) {
            throw new RuntimeException("Error obtaining PGConnection", e);
        }
    }

    @Override
    public Ticket createTicket(final TicketType type, final UUID creator, final String message) {
        Instant timestamp = TimeUtilities.nowTruncated();
        OpenAction action = new OpenAction(timestamp, creator, message);
        QualifiedType<OpenAction> actionType = QualifiedType.of(OpenAction.class).with(Json.class);

        Ticket createdTicket = this.jdbi.inTransaction(handle -> {
            List<String> queries = this.locator.queries("insert-ticket");

            int id = handle.createUpdate(queries.get(0))
                .bind("type_identifier", type.identifier())
                .bind("creator", creator)
                .bind("date", timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(Integer.class)
                .one();

            handle.createUpdate(queries.get(1))
                .bind("id", id)
                .bindByType("data", action, actionType)
                .execute();

            Ticket ticket = new SimpleTicket(id, type, creator, timestamp, new LinkedHashSet<>());
            ticket.withAction(action);

            return ticket;
        });

        logger.info("user {} created ticket {} - {} with message {}", creator, createdTicket.type().identifier(), createdTicket.id(), message);

        return createdTicket;
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
    public Optional<Ticket> selectTicket(final int id) {
        return Optional.ofNullable(this.selectTickets(id).get(id));
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
        final Set<TicketStatus> statuses,
        final @Nullable UUID creator,
        final @Nullable Instant since
    ) {
        List<Ticket> filteredTickets = this.jdbi.withHandle(handle -> {
            return handle.createQuery(this.locator.query("find-tickets"))
                .bindByType("creator", creator, UUID.class)
                .bindByType("since", since, Instant.class)
                .reduceRows(new TicketAccumulator())
                .collect(Collectors.toList());
        });

        filteredTickets.removeIf(ticket -> !statuses.contains(ticket.status()));

        return filteredTickets;
    }

    @Override
    public AssociatedAction selectActionWithTicketReference(int id) {
        return this.jdbi.withHandle(handle -> {
            return handle.createQuery(this.locator.query("select-action"))
                .bind("id", id)
                .mapTo(AssociatedAction.class)
                .first();
        });
    }

}
