package love.broccolai.tickets.core.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.EditAction;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

class SimpleModificationServiceTest {

    private final StorageService storageService = mock(StorageService.class);
    private final ModificationService modificationService = new SimpleModificationService(this.storageService);
    private Ticket ticket;

    @BeforeEach
    void setup() {
        this.ticket = new Ticket(1, UUID.randomUUID(), Instant.now(), null, "Test Message", new HashSet<>());
    }

    @Test
    void close() {
        this.modificationService.close(this.ticket, UUID.randomUUID(), null);
        assertThat(this.ticket.actions().size()).isEqualTo(1);
    }

    @Test
    void closeDuplicates() {
        UUID creator = UUID.randomUUID();

        this.modificationService.close(this.ticket, creator, null);
        this.modificationService.close(this.ticket, creator, null);

        assertThat(this.ticket.actions()).containsNoDuplicates();
    }

    @Test
    void edit() {
        UUID creator = UUID.randomUUID();
        EditAction action = this.modificationService.edit(this.ticket, creator, "New message");

        assertThat(this.ticket.message()).isEqualTo(action.message());
    }

}
