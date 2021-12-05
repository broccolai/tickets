package love.broccolai.tickets.core.service;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.AssignAction;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.core.utilities.TicketsJdbiPlugin;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.jdbi.v3.testing.junit5.JdbiH2Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.google.common.truth.Truth.assertThat;

//todo: REPLACE MIGRATE WITH FLYWAY
// - https://github.com/flyway/flyway/issues/3334
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseStorageServiceTest {

    @RegisterExtension
    private static final JdbiExtension H2_EXTENSION = new JdbiH2Extension() {
        @Override
        public String getUrl() {
            return super.getUrl() + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=2";
        }
    }.withPlugin(new TicketsJdbiPlugin());

    private StorageService storageService;

    @BeforeEach
    void setupEach() {
        this.storageService = new DatabaseStorageService(H2_EXTENSION.getJdbi());
    }

    @Test
    @Order(0)
    void migrate() {
        H2_EXTENSION.getJdbi().useHandle(handle -> {
            handle.createUpdate(ClasspathSqlLocator.create().locate("queries/migrations/V1__create_tables")).execute();
        });
    }

    @Test
    @Order(1)
    void createTicket() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Hello!");
        assertThat(ticket.id()).isEqualTo(1);
    }

    @Test
    @Order(2)
    void saveTicket() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Hello!");
        ticket.message("Hey!");

        this.storageService.saveTicket(ticket);
        Ticket loadedTicket = this.storageService.selectTicket(ticket.id());

        assertThat(ticket.message()).isEqualTo(loadedTicket.message());
    }

    @Test
    @Order(2)
    void saveTicketWithAssignAction() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Hello!");
        Action action = new AssignAction(Instant.now(), UUID.randomUUID(), UUID.randomUUID());

        ticket.actions().add(action);

        this.storageService.saveTicket(ticket);
        Ticket loadedTicket = this.storageService.selectTicket(ticket.id());

        assertThat(ticket.actions()).containsExactly(action);
    }

    @Test
    @Order(3)
    void selectTickets() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Test Message");
        Ticket loadedTicket = this.storageService.selectTicket(ticket.id());

        assertThat(ticket).isEqualTo(loadedTicket);
    }

}
