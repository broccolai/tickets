package love.broccolai.tickets.common.service;

import java.time.Instant;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.packaged.AssignAction;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.serialization.jdbi.ActionMapper;
import love.broccolai.tickets.common.utilities.PremadeActionRegistry;
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

    @BeforeEach
    void setupEach() {
        this.storageService = new DatabaseStorageService(this.h2Extension.getJdbi(), new ActionMapper(PremadeActionRegistry.create()));
    }

    @Test
    void createTicket() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Hello!");
        assertThat(ticket.id()).isEqualTo(1);
    }

    @Test
    void saveTicket() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Hello!");

        Action closeAction = new CloseAction(Instant.now(), UUID.randomUUID());
        ticket.withAction(closeAction);

        this.storageService.saveAction(ticket, closeAction);

        Optional<TicketStatus> loadedTicket = this.storageService.selectTicket(ticket.id())
            .map(Ticket::status);

        assertThat(loadedTicket).hasValue(ticket.status());
    }

    @Test
    void saveTicketWithAssignAction() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Hello!");
        Action action = new AssignAction(TimeUtilities.nowTruncated(), UUID.randomUUID(), UUID.randomUUID());

        ticket.withAction(action);

        this.storageService.saveAction(ticket, action);

        Set<Action> loadedActions = this.storageService.selectTicket(ticket.id())
            .map(Ticket::actions)
            .orElseGet(Set::of);

        assertThat(loadedActions).contains(action);
    }

    @Test
    void selectTickets() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Test Message");
        Optional<Ticket> loadedTicket = this.storageService.selectTicket(ticket.id());

        assertThat(loadedTicket).hasValue(ticket);
    }

    @Test
    void findTickets() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Test Message");
        Action closeAction = new CloseAction(Instant.now(), UUID.randomUUID());

        ticket.actions().add(closeAction);

        this.storageService.saveAction(ticket, closeAction);
        this.storageService.saveAction(ticket, closeAction);

        this.storageService.createTicket(UUID.randomUUID(), "TEST");
        this.storageService.createTicket(UUID.randomUUID(), "TEST");

        Collection<Ticket> foundTickets = this.storageService.findTickets(EnumSet.of(TicketStatus.OPEN), null, null);
        assertThat(foundTickets).hasSize(2);
    }

}
