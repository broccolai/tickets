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
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariDataSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import java.util.UUID;

import java.util.stream.Collectors;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.io.File;

@Singleton
public final class DatabaseStorageService implements StorageService {

    private final Multimap<Ticket, Interaction> queue = MultimapBuilder.hashKeys().hashSetValues().build();

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
                    .bind("player", soul.uuid().toString())
                    .bind("position", PositionMapper.valueOf(position))
                    .bind("status", TicketStatus.OPEN)
                    .bind("claimer", (String) null)
                    .execute();

            int id = handle.createQuery(queries[1])
                    .mapTo(Integer.class)
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);

            handle.createUpdate(queries[2])
                    .bind("ticket", id)
                    .bind("action", messageInteraction.action())
                    .bind("time", messageInteraction.time())
                    .bind("sender", messageInteraction.sender().toString())
                    .bind("message", messageInteraction.message())
                    .execute();

            return id;
        });
    }

    @Override
    public @NonNull Map<@NonNull Integer, @NonNull Ticket> tickets(
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
    public @NonNull Map<@NonNull Integer, @NonNull Ticket> findTickets(@NonNull final Collection<TicketStatus> statuses) {
        return this.jdbi.withHandle(handle -> {
            String[] queries = SQLQueries.SELECT_TICKETS_STATUSES.get();

            return handle.createQuery(queries[0])
                    .bindList("statuses", statuses)
                    .mapTo(Ticket.class)
                    .collect(Collectors.toMap(Ticket::id, ticket -> ticket));
        });
    }

    @Override
    public @NonNull Map<@NonNull Integer, @NonNull Ticket> findTickets(
            final @NonNull Soul soul,
            final @NonNull Collection<TicketStatus> statuses
    ) {
        return this.jdbi.withHandle(handle -> {
            String[] queries = SQLQueries.SELECT_TICKETS_SOUL_STATUSES.get();

            return handle.createQuery(queries[0])
                    .bind("player", soul.uuid().toString())
                    .bindList("statuses", statuses)
                    .mapTo(Ticket.class)
                    .collect(Collectors.toMap(Ticket::id, ticket -> ticket));
        });
    }

    @Override
    public void updateTickets(@NonNull final Collection<Ticket> tickets) {
        this.jdbi.useHandle(handle -> {
            PreparedBatch batch = handle.prepareBatch(SQLQueries.UPDATE_TICKET.get()[0]);

            for (final Ticket ticket : tickets) {
                batch.bind("id", ticket.id())
                        .bind("status", ticket.status())
                        .bind("claimer", ticket.claimer().map(Object::toString).orElse(null))
                        .add();
            }

            batch.execute();
        });
    }

    @Override
    public void saveInteractions(final @NonNull Multimap<Ticket, Interaction> interactionMultimap) {
        this.jdbi.useHandle(handle -> {
            PreparedBatch batch = handle.prepareBatch(SQLQueries.INSERT_INTERACTION.get()[0]);

            interactionMultimap.forEach((ticket, interaction) -> {
                batch.bind("ticket", ticket.id())
                        .bind("action", interaction.action())
                        .bind("time", interaction.time())
                        .bind("sender", interaction.sender().toString());

                if (interaction instanceof MessageInteraction) {
                    MessageInteraction messageInteraction = (MessageInteraction) interaction;
                    batch.bind("message", messageInteraction.message());
                } else {
                    batch.bind("message", (String) null);
                }

                batch.add();
            });

            batch.execute();
        });
    }

    @Override
    public @NonNull Collection<@NonNull Interaction> interactions(@NonNull final Ticket ticket) {
        return this.jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.SELECT_INTERACTIONS.get()[0])
                    .bind("ticket", ticket.id())
                    .mapTo(Interaction.class)
                    .list();
        });
    }

    @Override
    public @NonNull Collection<Component> notifications(final @NonNull Soul soul) {
        return this.jdbi.withHandle(handle -> {
            String[] queries = SQLQueries.NOTIFICATIONS.get();

            Collection<Component> components = handle.createQuery(queries[0])
                        .bind("uuid", soul.uuid().toString())
                        .mapTo(Component.class)
                        .list();

            handle.createUpdate(queries[1])
                    .bind("uuid", soul.uuid().toString())
                    .execute();

            return components;
        });
    }

    @Override
    public void saveNotification(final @NonNull Soul soul, final @NonNull Component component) {
        this.jdbi.useHandle(handle -> {
            handle.createUpdate(SQLQueries.INSERT_NOTIFICATION.get()[0])
                    .bind("uuid", soul.uuid().toString())
                    .bind("message", ComponentMapper.MINI.serialize(component))
                    .execute();
        });
    }

    @Override
    public @NonNull Map<@NonNull UUID, @NonNull Integer> highscores(final @NonNull ChronoUnit chronoUnit) {
        return this.jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.HIGHSCORES.get()[0])
                    .bind("time", LocalDateTime.now().minus(1, chronoUnit))
                    .reduceRows(new HashMap<>(), (map, rowView) -> {
                        map.put(
                                rowView.getColumn("claimer", UUID.class),
                                rowView.getColumn("num", Integer.class)
                        );

                        return map;
                    });
        });
    }

    @Override
    public void queue(@NonNull final Ticket ticket, @NonNull final Interaction interaction) {
        this.queue.put(ticket, interaction);
    }

    @Override
    public void clear() {
        this.updateTickets(this.queue.keys());
        this.saveInteractions(this.queue);
        this.queue.clear();
    }

    @Override
    public void dispose() {
        this.clear();
        this.dataSource.close();
    }

}
