package love.broccolai.tickets.common.service;

import java.util.LinkedHashSet;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.model.SimpleTicket;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.mockito.Mockito.mock;

class SimpleModificationServiceTest {

    private final StorageService storageService = mock(StorageService.class);
    private final ModificationService modificationService = new SimpleModificationService(this.storageService);

    private SimpleTicket ticket;

    @BeforeEach
    void setup() {
        this.ticket = new SimpleTicket(
            1,
            UUID.randomUUID(),
            TimeUtilities.nowTruncated(),
            new LinkedHashSet<>()
        );

        Action openAction = new OpenAction(TimeUtilities.nowTruncated(), UUID.randomUUID(), "Test Message");
        this.ticket.withAction(openAction);
    }

    @Test
    void close() {
        this.modificationService.close(this.ticket, UUID.randomUUID());

        assertThat(this.ticket.actions()).hasSize(2);
        assertThat(this.ticket.status()).isEqualTo(TicketStatus.CLOSED);
    }

    @Test
    void edit() {
        CommentAction action = this.modificationService.comment(
            this.ticket,
            UUID.randomUUID(),
            "New message"
        );

        assertThat(this.ticket.actions()).hasSize(2);
        assertThat(this.ticket.message()).isEqualTo(action.message());
    }

    @Test
    void assign() {
        UUID assignee = UUID.randomUUID();

        this.modificationService.assign(this.ticket, assignee, assignee);

        assertThat(this.ticket.assignee()).hasValue(assignee);
    }

}
