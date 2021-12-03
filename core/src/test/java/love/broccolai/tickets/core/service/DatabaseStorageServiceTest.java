package love.broccolai.tickets.core.service;

import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.service.StorageService;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.jdbi.v3.testing.junit5.JdbiH2Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.google.common.truth.Truth.assertThat;

//todo: REPLACE MIGRATE WITH FLYWAY
// - https://github.com/flyway/flyway/issues/3334
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    void setup() {
        this.storageService = new DatabaseStorageService(H2_EXTENSION.getJdbi());
    }

    @Test
    void migrate() {
        H2_EXTENSION.getJdbi().useHandle(handle -> {
            handle.createUpdate(ClasspathSqlLocator.create().locate("queries/migrations/V1__create_tables")).execute();
        });
    }

    @Test
    void createTicket() {
        Ticket ticket = this.storageService.createTicket(UUID.randomUUID(), "Hello!");
        assertThat(ticket.id()).isEqualTo(1);
    }

}
