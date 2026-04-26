package love.broccolai.tickets.common.service;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.ActionCodec;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.model.action.packaged.TicketAssigned;
import love.broccolai.tickets.api.model.action.packaged.TicketClosed;
import love.broccolai.tickets.api.model.action.packaged.TicketComponentAttached;
import love.broccolai.tickets.api.model.action.packaged.TicketComponentDetached;
import love.broccolai.tickets.api.model.action.packaged.TicketOpened;
import love.broccolai.tickets.api.model.action.packaged.TicketReopened;
import love.broccolai.tickets.api.model.action.packaged.TicketUnassigned;
import love.broccolai.tickets.api.model.component.ComponentCodec;
import love.broccolai.tickets.api.model.component.ComponentKey;
import love.broccolai.tickets.api.model.component.TicketComponent;
import love.broccolai.tickets.api.model.component.TicketForm;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.api.service.TicketSearch;
import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import love.broccolai.tickets.common.registry.SimpleActionRegistry;
import love.broccolai.tickets.common.registry.SimpleComponentRegistry;
import love.broccolai.tickets.common.utilities.PremadeTicketTypeRegistry;
import love.broccolai.tickets.common.utilities.PremadeTickets;
import love.broccolai.tickets.common.utilities.TicketsH2Extension;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.google.common.truth.Truth.assertThat;

class DatabaseStorageServiceTest {

    @RegisterExtension
    JdbiExtension h2Extension = TicketsH2Extension.instance();

    private StorageService storageService;
    private SimpleComponentRegistry componentRegistry;
    private SimpleActionRegistry actionRegistry;

    @BeforeEach
    void setupEach() {
        this.componentRegistry = new SimpleComponentRegistry(PremadeTicketTypeRegistry.create());
        this.componentRegistry.register(ComponentCodec.of(
            TicketPriority.KEY,
            component -> Map.of("level", component.level()),
            properties -> new TicketPriority(properties.require("level", Number.class).intValue())
        ));

        this.actionRegistry = new SimpleActionRegistry(this.componentRegistry);
        this.actionRegistry.register(ActionCodec.of(
            TicketFlagged.TYPE,
            TicketFlagged.class,
            action -> Map.of("reason", action.reason()),
            (date, creator, properties) -> new TicketFlagged(date, creator, properties.require("reason", String.class))
        ));
        this.storageService = new DatabaseStorageService(
            this.h2Extension.getJdbi(),
            this.componentRegistry,
            this.actionRegistry,
            new DatabaseConfiguration()
        );
    }

    @Test
    void createTicket() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);

        assertThat(ticket.id()).isEqualTo(1);
    }

    @Test
    void saveTicket() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);

        TicketAction closeAction = new TicketClosed(Instant.now(), UUID.randomUUID());

        this.storageService.appendAction(ticket, closeAction);

        Optional<TicketStatus> loadedTicket = this.storageService.selectTicket(ticket.id())
            .map(Ticket::status);

        assertThat(loadedTicket).hasValue(TicketStatus.CLOSED);
    }

    @Test
    void saveTicketWithAssignEvent() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);
        TicketAction action = new TicketAssigned(TimeUtilities.nowTruncated(), UUID.randomUUID(), UUID.randomUUID());

        this.storageService.appendAction(ticket, action);

        List<TicketAction> loadedActions = this.storageService.selectTicket(ticket.id())
            .map(Ticket::actions)
            .orElseGet(List::of);

        assertThat(loadedActions).contains(action);
    }

    @Test
    void saveTicketWithReopenAction() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);
        TicketAction close = new TicketClosed(TimeUtilities.nowTruncated(), UUID.randomUUID());

        this.storageService.appendAction(ticket, close);
        Ticket closed = this.storageService.selectTicket(ticket.id()).orElseThrow();

        this.storageService.appendAction(
            closed,
            new TicketReopened(TimeUtilities.nowTruncated(), UUID.randomUUID())
        );

        Ticket loaded = this.storageService.selectTicket(ticket.id()).orElseThrow();

        assertThat(loaded.status()).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    void saveTicketWithUnassignEvent() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);
        TicketAction assign = new TicketAssigned(TimeUtilities.nowTruncated(), UUID.randomUUID(), UUID.randomUUID());

        this.storageService.appendAction(ticket, assign);
        Ticket assigned = this.storageService.selectTicket(ticket.id()).orElseThrow();

        this.storageService.appendAction(
            assigned,
            new TicketUnassigned(TimeUtilities.nowTruncated(), UUID.randomUUID())
        );

        Ticket loaded = this.storageService.selectTicket(ticket.id()).orElseThrow();

        assertThat(loaded.status()).isEqualTo(TicketStatus.OPEN);
        assertThat(loaded.assignee()).isEmpty();
    }

    @Test
    void selectTickets() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);
        Optional<Ticket> loadedTicket = this.storageService.selectTicket(ticket.id());

        assertThat(loadedTicket).hasValue(ticket);
    }

    @Test
    void findTickets() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);
        TicketAction closeAction = new TicketClosed(Instant.now(), UUID.randomUUID());

        this.storageService.appendAction(ticket, closeAction);
        this.storageService.appendAction(ticket, closeAction);

        PremadeTickets.createTicket(this.storageService);
        PremadeTickets.createTicket(this.storageService);

        Collection<Ticket> foundTickets = this.storageService.findTickets(
            TicketSearch.open()
        );

        assertThat(foundTickets).hasSize(2);
    }

    @Test
    void findTicketsUsesLatestStatusEvent() {
        Ticket open = PremadeTickets.createTicket(this.storageService);
        Ticket picked = PremadeTickets.createTicket(this.storageService);
        Ticket closed = PremadeTickets.createTicket(this.storageService);

        this.storageService.appendAction(
            picked,
            new TicketAssigned(TimeUtilities.nowTruncated(), UUID.randomUUID(), UUID.randomUUID())
        );
        this.storageService.appendAction(
            closed,
            new TicketClosed(TimeUtilities.nowTruncated(), UUID.randomUUID())
        );

        assertThat(this.ticketIds(TicketSearch.open()))
            .containsExactly(open.id());
        assertThat(this.ticketIds(TicketSearch.picked()))
            .containsExactly(picked.id());
        assertThat(this.ticketIds(TicketSearch.closed()))
            .containsExactly(closed.id());
    }

    @Test
    void findTicketsFiltersByCreator() {
        UUID creator = UUID.randomUUID();
        Ticket matching = this.storageService.createTicket(
            creator,
            PremadeTickets.ticketType(),
            PremadeTickets.ticketForm()
        );

        PremadeTickets.createTicket(this.storageService);

        assertThat(this.ticketIds(TicketSearch.open().createdBy(creator)))
            .containsExactly(matching.id());
    }

    @Test
    void findTicketsFiltersByCreatedDate() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);

        assertThat(this.ticketIds(TicketSearch.open().since(ticket.date().minusSeconds(1))))
            .containsExactly(ticket.id());
        assertThat(this.ticketIds(TicketSearch.open().since(ticket.date().plusSeconds(1))))
            .isEmpty();
    }

    @Test
    void roundTripsCustomComponentAttachment() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);
        TicketPriority priority = new TicketPriority(3);
        TicketAction action = new TicketComponentAttached(TimeUtilities.nowTruncated(), UUID.randomUUID(), priority);

        this.storageService.appendAction(ticket, action);

        Ticket loaded = this.storageService.selectTicket(ticket.id()).orElseThrow();

        assertThat(loaded.components().find(TicketPriority.KEY)).hasValue(priority);
        assertThat(loaded.actions()).contains(action);
    }

    @Test
    void roundTripsCustomComponentDetachment() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);
        TicketAction attach = new TicketComponentAttached(
            TimeUtilities.nowTruncated(),
            UUID.randomUUID(),
            new TicketPriority(3)
        );

        this.storageService.appendAction(ticket, attach);
        Ticket attached = this.storageService.selectTicket(ticket.id()).orElseThrow();

        this.storageService.appendAction(
            attached,
            new TicketComponentDetached(TimeUtilities.nowTruncated(), UUID.randomUUID(), TicketPriority.KEY)
        );

        Ticket loaded = this.storageService.selectTicket(ticket.id()).orElseThrow();

        assertThat(loaded.components().find(TicketPriority.KEY)).isEmpty();
    }

    @Test
    void loadsOpenedEventFromEventPayloadInsteadOfProjectedComponents() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);
        TicketAction replacementForm = new TicketComponentAttached(
            TimeUtilities.nowTruncated(),
            UUID.randomUUID(),
            new TicketForm(PremadeTickets.ticketForm().with("message", "This is the projected form now"))
        );

        this.storageService.appendAction(ticket, replacementForm);

        Ticket loaded = this.storageService.selectTicket(ticket.id()).orElseThrow();
        TicketOpened opened = loaded.actions()
            .stream()
            .filter(TicketOpened.class::isInstance)
            .map(TicketOpened.class::cast)
            .findFirst()
            .orElseThrow();

        assertThat(opened.form().require("message", String.class)).isEqualTo("What is the meaning of life?");
        assertThat(loaded.form().require("message", String.class)).isEqualTo("This is the projected form now");
    }

    @Test
    void ticketStateIsReplayedFromTimeline() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);
        TicketAction assign = new TicketAssigned(TimeUtilities.nowTruncated(), UUID.randomUUID(), UUID.randomUUID());

        this.storageService.appendAction(ticket, assign);

        Ticket loaded = this.storageService.selectTicket(ticket.id()).orElseThrow();

        assertThat(loaded.assignee()).hasValue(((TicketAssigned) assign).assignee());
        assertThat(loaded.status()).isEqualTo(TicketStatus.PICKED);
    }

    @Test
    void roundTripsCustomActionCodec() {
        Ticket ticket = PremadeTickets.createTicket(this.storageService);
        TicketFlagged action = new TicketFlagged(TimeUtilities.nowTruncated(), UUID.randomUUID(), "needs staff eyes");

        this.storageService.appendAction(ticket, action);

        Ticket loaded = this.storageService.selectTicket(ticket.id()).orElseThrow();

        assertThat(loaded.actions()).contains(action);
    }

    @Test
    void findProfileIgnoresUsernameCase() {
        Profile profile = new Profile(UUID.randomUUID(), "Broccoli");

        this.storageService.insertProfile(profile);

        assertThat(this.storageService.findProfile("broccoli")).hasValue(profile);
    }

    private List<Integer> ticketIds(final TicketSearch search) {
        return this.storageService.findTickets(search)
            .stream()
            .map(Ticket::id)
            .toList();
    }

    private record TicketPriority(
        int level
    ) implements TicketComponent {

        private static final ComponentKey<TicketPriority> KEY = ComponentKey.of(
            "test:priority",
            TicketPriority.class
        );

        @Override
        public ComponentKey<TicketPriority> key() {
            return KEY;
        }
    }

    private record TicketFlagged(
        Instant date,
        UUID creator,
        String reason
    ) implements TicketAction {

        private static final String TYPE = "test:flagged";
    }
}
