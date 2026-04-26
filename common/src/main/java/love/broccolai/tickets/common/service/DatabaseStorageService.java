package love.broccolai.tickets.common.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.api.action.TicketActionListener;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.ActionRegistry;
import love.broccolai.tickets.api.model.action.AssociatedTicketAction;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.model.action.packaged.TicketOpened;
import love.broccolai.tickets.api.model.component.ComponentProperties;
import love.broccolai.tickets.api.model.component.ComponentRegistry;
import love.broccolai.tickets.api.model.component.TicketComponents;
import love.broccolai.tickets.api.model.component.TicketType;
import love.broccolai.tickets.api.model.format.TicketFormData;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.api.service.TicketSearch;
import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import love.broccolai.tickets.common.model.SimpleTicket;
import love.broccolai.tickets.common.registry.SimpleActionRegistry;
import love.broccolai.tickets.common.service.storage.StoredTicketAction;
import love.broccolai.tickets.common.service.storage.TicketActionStore;
import love.broccolai.tickets.common.service.storage.TicketNotificationListener;
import love.broccolai.tickets.common.service.storage.TicketPropertyStore;
import love.broccolai.tickets.common.service.storage.TicketQueryIndex;
import love.broccolai.tickets.common.utilities.QueriesLocator;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@NullMarked
public final class DatabaseStorageService implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseStorageService.class);

    private final Jdbi jdbi;
    private final ComponentRegistry componentRegistry;
    private final QueriesLocator locator;
    private final TicketActionStore actionStore;
    private final TicketQueryIndex queryIndex;
    private final TicketNotificationListener notificationListener;

    @Inject
    public DatabaseStorageService(
        final Jdbi jdbi,
        final ComponentRegistry componentRegistry,
        final ActionRegistry actionRegistry,
        final DatabaseConfiguration configuration
    ) {
        this.jdbi = jdbi;
        this.componentRegistry = componentRegistry;
        this.locator = new QueriesLocator(configuration.type);
        TicketPropertyStore properties = new TicketPropertyStore(this.locator);

        this.actionStore = new TicketActionStore(this.locator, actionRegistry, properties);
        this.queryIndex = new TicketQueryIndex(this.locator);
        this.notificationListener = new TicketNotificationListener(
            jdbi,
            configuration.type,
            this::selectEventWithTicketReference
        );
    }

    public DatabaseStorageService(
        final Jdbi jdbi,
        final ComponentRegistry componentRegistry,
        final DatabaseConfiguration configuration
    ) {
        this(jdbi, componentRegistry, new SimpleActionRegistry(componentRegistry), configuration);
    }

    @Override
    public void addTicketActionRelay(final TicketActionListener listener) {
        this.notificationListener.add(listener);
    }

    @Override
    public Ticket createTicket(final UUID creator, final TicketFormat type, final TicketFormData form) {
        if (!type.identifier().equals(form.formatIdentifier())) {
            throw new IllegalArgumentException("Ticket form data does not match ticket type: " + type.identifier());
        }

        Instant timestamp = TimeUtilities.nowTruncated();
        TicketOpened action = new TicketOpened(timestamp, creator, form);

        Ticket createdTicket = this.jdbi.inTransaction(handle -> {
            int ticketId = handle.createUpdate(this.locator.query("insert-ticket"))
                .bindByType("created_at", timestamp, Instant.class)
                .executeAndReturnGeneratedKeys()
                .mapTo(Integer.class)
                .one();

            Ticket ticket = new SimpleTicket(ticketId, TicketComponents.EMPTY.with(new TicketType(type)), List.of())
                .withAction(action);
            this.actionStore.insert(handle, ticketId, action);
            this.queryIndex.replace(handle, ticketId, ticket.components(), action.date());

            return ticket;
        });

        logger.info(
            "user {} created ticket {} - {} with form {}",
            creator,
            createdTicket.type().identifier(),
            createdTicket.id(),
            createdTicket.form()
        );

        return createdTicket;
    }

    @Override
    public void appendAction(final Ticket ticket, final TicketAction action) {
        this.jdbi.useTransaction(handle -> {
            this.actionStore.insert(handle, ticket.id(), action);

            TicketComponents components = this.replay(this.actionStore.loadAll(handle, ticket.id()));
            this.queryIndex.replace(handle, ticket.id(), components, action.date());
        });
    }

    @Override
    public Optional<Ticket> selectTicket(final int ticketId) {
        return Optional.ofNullable(this.selectTickets(ticketId).get(ticketId));
    }

    @Override
    public Map<Integer, Ticket> selectTickets(final int... ticketIds) {
        if (ticketIds.length == 0) {
            return Map.of();
        }

        return this.jdbi.withHandle(handle -> {
            Map<Integer, Ticket> tickets = new HashMap<>();
            for (int ticketId : ticketIds) {
                this.loadTicket(handle, ticketId).ifPresent(ticket -> tickets.put(ticketId, ticket));
            }

            return tickets;
        });
    }

    @Override
    public Collection<Ticket> findTickets(final TicketSearch search) {
        if (search.statuses().isEmpty()) {
            return List.of();
        }

        return this.jdbi.withHandle(handle -> handle.createQuery(this.locator.query("find-tickets"))
            .bindByType("creator", search.creator(), UUID.class)
            .bindByType("since", search.since(), Instant.class)
            .bindList("statuses", this.statusIdentifiers(search.statuses()))
            .mapTo(Integer.class)
            .list()
            .stream()
            .map(ticketId -> this.loadTicket(handle, ticketId).orElseThrow())
            .toList());
    }

    @Override
    public Collection<Profile> loadProfiles(final Collection<UUID> uniqueIds) {
        if (uniqueIds.isEmpty()) {
            return List.of();
        }

        return this.jdbi.withHandle(handle -> handle.createQuery(this.locator.query("profile/select-profiles"))
            .bindList("ids", uniqueIds)
            .mapTo(Profile.class)
            .list());
    }

    @Override
    public Optional<Profile> findProfile(final String name) {
        return this.jdbi.withHandle(handle -> handle.createQuery(this.locator.query("profile/find-profile"))
            .bind("username", name)
            .mapTo(Profile.class)
            .findFirst());
    }

    @Override
    public void insertProfile(final Profile profile) {
        this.jdbi.useHandle(handle -> {
            handle.createUpdate(this.locator.query("profile/create-profile"))
                .bindByType("uuid", profile.uuid(), UUID.class)
                .bind("username", profile.username())
                .execute();
        });
    }

    @Override
    public void updateProfile(final Profile profile) {
        this.jdbi.useHandle(handle -> {
            handle.createUpdate(this.locator.query("profile/update-profile"))
                .bindByType("uuid", profile.uuid(), UUID.class)
                .bind("username", profile.username())
                .execute();
        });
    }

    private Optional<Ticket> loadTicket(final Handle handle, final int ticketId) {
        boolean exists = handle.createQuery(this.locator.query("ticket-exists"))
            .bind("id", ticketId)
            .mapTo(Integer.class)
            .one() > 0;

        if (!exists) {
            return Optional.empty();
        }

        List<TicketAction> actions = this.actionStore.loadAll(handle, ticketId);

        if (actions.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new SimpleTicket(ticketId, this.replay(actions), actions));
    }

    private AssociatedTicketAction selectEventWithTicketReference(final int actionId) {
        return this.jdbi.withHandle(handle -> {
            StoredTicketAction storedAction = this.actionStore.load(handle, actionId);

            return new AssociatedTicketAction(storedAction.ticketId(), storedAction.action());
        });
    }

    private List<String> statusIdentifiers(final Collection<TicketStatus> statuses) {
        return statuses.stream()
            .map(Enum::name)
            .toList();
    }

    private TicketComponents replay(final Collection<TicketAction> actions) {
        TicketComponents components = TicketComponents.EMPTY;

        for (TicketAction action : actions) {
            if (action instanceof TicketOpened opened) {
                components = components.with(this.type(opened));
            }

            components = action.apply(components);
        }

        return components;
    }

    private TicketType type(final TicketOpened action) {
        return (TicketType) this.componentRegistry.requireDecoded(
            TicketType.KEY.value(),
            new ComponentProperties(Map.of("identifier", action.form().formatIdentifier()))
        );
    }
}
