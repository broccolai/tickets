package love.broccolai.tickets.core.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.EditAction;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.mockito.Mockito.mock;

class SimpleModificationServiceTest {

    private final StorageService storageService = mock(StorageService.class);
    private final ModificationService modificationService = new SimpleModificationService(this.storageService);

    private Ticket ticket;

    @BeforeEach
    void setup() {
        this.ticket = new Ticket(
                1,
                TicketStatus.OPEN,
                UUID.randomUUID(),
                Instant.now(),
                null,
                "Test Message",
                new ArrayList<>()
        );
    }

    @Test
    void close() {
        this.modificationService.close(this.ticket, UUID.randomUUID(), null);
        assertThat(this.ticket.actions().size()).isEqualTo(1);
    }

    @Test
    void edit() {
        UUID creator = UUID.randomUUID();
        EditAction action = this.modificationService.edit(this.ticket, creator, "New message");

        assertThat(this.ticket.message()).isEqualTo(action.message());
    }

    @Test
    void assign() {
        UUID creator = UUID.randomUUID();
        UUID assignee = UUID.randomUUID();

        this.modificationService.assign(this.ticket, creator, assignee);

        assertThat(this.ticket.assignee()).hasValue(assignee);
    }

}
