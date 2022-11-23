package love.broccolai.tickets.core.service;

import java.util.Collection;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.AssignAction;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.core.utilities.TimeUtilities;
import love.broccolai.tickets.core.utilities.TicketsH2Extension;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.google.common.truth.Truth.assertThat;

class DatabaseStorageServiceTest {

    @RegisterExtension
    private static final JdbiExtension H2_EXTENSION = TicketsH2Extension.instance();

    private StorageService storageService;

    @BeforeEach
    void setupEach() {
        this.storageService = new DatabaseStorageService(H2_EXTENSION.getJdbi());
    }

    @Test
    void createTicket() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Hello!");
        assertThat(ticket.id()).isEqualTo(1);
    }

    @Test
    void saveTicket() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Hello!");
        ticket.message("Hey!");

        this.storageService.saveTicket(ticket);
        Ticket loadedTicket = this.storageService.selectTicket(ticket.id());

        assertThat(ticket.message()).isEqualTo(loadedTicket.message());
    }

    @Test
    void saveTicketWithAssignAction() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Hello!");
        Action action = new AssignAction(TimeUtilities.nowTruncated(), UUID.randomUUID(), UUID.randomUUID());

        ticket.actions().add(action);

        this.storageService.saveTicket(ticket);
        Ticket loadedTicket = this.storageService.selectTicket(ticket.id());

        assertThat(loadedTicket.actions()).containsExactly(action);
    }

    @Test
    void selectTickets() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Test Message");
        Ticket loadedTicket = this.storageService.selectTicket(ticket.id());

        assertThat(ticket).isEqualTo(loadedTicket);
    }

    @Test
    void findTickets() {
        Ticket closedTicket = this.storageService.createTicket(UUID.randomUUID(), "Test Message");
        closedTicket.status(TicketStatus.CLOSED);
        this.storageService.saveTicket(closedTicket);

        this.storageService.createTicket(UUID.randomUUID(), "TEST");
        this.storageService.createTicket(UUID.randomUUID(), "TEST");

        Collection<Ticket> foundTickets = this.storageService.findTickets(TicketStatus.OPEN, null, null);
        assertThat(foundTickets).hasSize(2);
    }

}
