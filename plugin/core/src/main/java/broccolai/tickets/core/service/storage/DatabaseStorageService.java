package broccolai.tickets.core.service.storage;

import broccolai.tickets.api.model.interaction.Interaction;
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

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;
import java.io.File;

@Singleton
public final class DatabaseStorageService implements StorageService {

    private final Jdbi jdbi;

    @Inject
    public DatabaseStorageService(
            final @NonNull File folder,
            final @NonNull ClassLoader classLoader,
            final @NonNull MainConfiguration mainConfiguration
    ) {
        DataSource dataSource = new HikariDataSource(mainConfiguration.storageConfiguration.asHikari(folder));

        Flyway.configure(classLoader)
                .baselineVersion("0")
                .baselineOnMigrate(true)
                .locations("queries/migrations")
                .dataSource(dataSource)
                .validateOnMigrate(false)
                .load()
                .migrate();

        this.jdbi = Jdbi.create(dataSource)
            .registerColumnMapper(Component.class, new ComponentMapper())
            .registerColumnMapper(Position.class, new PositionMapper())
            .registerRowMapper(Interaction.class, new InteractionMapper())
            .registerRowMapper(Ticket.class, new TicketMapper());
    }

    @Override
    public int create(@NonNull final Soul soul, @NonNull final Position position) {
        return this.jdbi.withHandle(handle -> {
            String[] queries = SQLQueries.INSERT_TICKET.get();

            handle.createUpdate(queries[0])
                    .bind("player", soul.uuid())
                    .bind("position", PositionMapper.valueOf(position))
                    .bind("status", TicketStatus.OPEN)
                    .bind("picker", (UUID) null)
                    .execute();

            return handle.createQuery(queries[1])
                    .mapTo(Integer.class)
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
        });
    }

    @Override
    public @NonNull Map<Integer, Ticket> tickets(
            final @NonNull Collection<@NonNull Integer> id
    ) {
        //todo
        return null;
    }

    @Override
    public @NonNull Map<Integer, Ticket> tickets(
            final @NonNull Soul soul,
            final @NonNull Collection<TicketStatus> statuses
    ) {
        //todo
        return null;
    }

    @Override
    public int countTickets(final @NonNull Collection<TicketStatus> statuses) {
        //todo
        return 0;
    }

    @Override
    public @NonNull Collection<Component> notifications(final @NonNull Soul soul) {
        //todo
        return null;
    }

}
