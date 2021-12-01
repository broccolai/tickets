package love.broccolai.tickets.core.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.service.ModificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

class SimpleModificationServiceTest {

    ModificationService modificationService = new SimpleModificationService();
    Ticket ticket;

    @BeforeEach
    void setup() {
        this.ticket = new Ticket(1, UUID.randomUUID(), Instant.now(), "Test Message", new HashSet<>());
    }

    @Test
    void close() {
        this.modificationService.close(this.ticket, UUID.randomUUID());
        assertThat(this.ticket.actions().size()).isEqualTo(1);
    }

    @Test
    void closeDuplicates() {
        UUID creator = UUID.randomUUID();

        this.modificationService.close(this.ticket, creator);
        this.modificationService.close(this.ticket, creator);

        assertThat(this.ticket.actions()).containsNoDuplicates();
    }

}
