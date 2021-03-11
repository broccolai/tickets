package broccolai.tickets.core.service.storage;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.core.configuration.MainConfiguration;
import broccolai.tickets.core.storage.SQLQueries;
import broccolai.tickets.core.storage.mapper.ComponentMapper;
import broccolai.tickets.core.storage.mapper.InteractionMapper;
import broccolai.tickets.core.storage.mapper.PositionMapper;
import broccolai.tickets.core.storage.mapper.TicketMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Collection;
import java.util.Map;

import java.util.UUID;

import java.util.stream.Collectors;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;

import java.io.File;

@Singleton
public final class DatabaseStorageService implements StorageService {

    private final HikariDataSource dataSource;
    private final Jdbi jdbi;

    @Inject
    public DatabaseStorageService(
            final @NonNull File folder,
            final @NonNull ClassLoader classLoader,
            final @NonNull MainConfiguration mainConfiguration
    ) {
        this.dataSource = new HikariDataSource(mainConfiguration.storageConfiguration.asHikari(folder));

        Flyway.configure(classLoader)
                .baselineVersion("0")
                .baselineOnMigrate(true)
                .locations("queries/migrations")
                .dataSource(this.dataSource)
                .validateOnMigrate(false)
                .load()
                .migrate();

        this.jdbi = Jdbi.create(this.dataSource)
            .registerColumnMapper(Component.class, new ComponentMapper())
            .registerColumnMapper(Position.class, new PositionMapper())
            .registerRowMapper(Interaction.class, new InteractionMapper())
            .registerRowMapper(Ticket.class, new TicketMapper());
    }

    @Override
    public int create(
            final @NonNull Soul soul,
            final @NonNull Position position,
            final @NonNull MessageInteraction messageInteraction
    ) {
        return this.jdbi.withHandle(handle -> {
            String[] queries = SQLQueries.INSERT_TICKET.get();

            handle.createUpdate(queries[0])
                    .bind("player", soul.uuid())
                    .bind("position", PositionMapper.valueOf(position))
                    .bind("status", TicketStatus.OPEN)
                    .bind("picker", (UUID) null)
                    .execute();

            int id = handle.createQuery(queries[1])
                    .mapTo(Integer.class)
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);

            handle.createUpdate(queries[2])
                    .bind("ticket", id)
                    .bind("action", messageInteraction.action())
                    .bind("time", messageInteraction.time())
                    .bind("sender", messageInteraction.sender())
                    .bind("message", messageInteraction.message())
                    .execute();

            return id;
        });
    }

    @Override
    public @NonNull Map<Integer, Ticket> tickets(
            final @NonNull Collection<@NonNull Integer> ids
    ) {
        return this.jdbi.withHandle(handle -> {
            String[] queries = SQLQueries.SELECT_TICKETS.get();

            return handle.createQuery(queries[0])
                    .bindList("ids", ids)
                    .mapTo(Ticket.class)
                    .collect(Collectors.toMap(Ticket::id, ticket -> ticket));
        });
    }

    @Override
    public @NonNull Map<Integer, Ticket> tickets(
            final @NonNull Soul soul,
            final @NonNull Collection<TicketStatus> statuses
    ) {
        return this.jdbi.withHandle(handle -> {
            String[] queries = SQLQueries.SELECT_TICKETS_SOUL_STATUSES.get();

            return handle.createQuery(queries[0])
                    .bind("player", soul.uuid())
                    .bindList("statuses", statuses)
                    .mapTo(Ticket.class)
                    .collect(Collectors.toMap(Ticket::id, ticket -> ticket));
        });
    }

    @Override
    public int countTickets(final @NonNull Collection<TicketStatus> statuses) {
        return this.jdbi.withHandle(handle -> {
            String[] queries = SQLQueries.COUNT_TICKETS_STATUSES.get();

            return handle.createQuery(queries[0])
                    .bindList("statuses", statuses)
                    .mapTo(Integer.class)
                    .first();
        });
    }

    @Override
    public @NonNull Collection<Component> notifications(final @NonNull Soul soul) {
        //todo
        return null;
    }

    @Override
    public void dispose() {
        this.dataSource.close();
    }

}
