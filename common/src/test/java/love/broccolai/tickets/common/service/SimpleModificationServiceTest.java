package love.broccolai.tickets.common.service;

import java.util.LinkedHashSet;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.api.model.format.TicketFormatContent;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.api.utilities.Pair;
import love.broccolai.tickets.common.model.SimpleTicket;
import love.broccolai.tickets.common.utilities.PremadeTickets;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

class SimpleModificationServiceTest {

    private final StorageService storageService = mock(StorageService.class);
    private final ModificationService modificationService = new SimpleModificationService(this.storageService);

    private SimpleTicket ticket;

    @BeforeEach
    void setup() {
        this.ticket = new SimpleTicket(
            1,
            PremadeTickets.ticketType(),
            UUID.randomUUID(),
            TimeUtilities.nowTruncated(),
            new LinkedHashSet<>()
        );

        Action openAction = new OpenAction(TimeUtilities.nowTruncated(), UUID.randomUUID(), TicketFormatContent.of(
            Pair.of("message", "hello")
        ));

        this.ticket.withAction(openAction);
    }

    @Test
    void close() {
        this.modificationService.close(this.ticket, UUID.randomUUID());

        assertThat(this.ticket.actions()).hasSize(2);
        assertThat(this.ticket.status()).isEqualTo(TicketStatus.CLOSED);
    }

    //todo
    @Test
    @Disabled
    void edit() {
        CommentAction action = this.modificationService.comment(
            this.ticket,
            UUID.randomUUID(),
            "New message"
        );

        assertThat(this.ticket.actions()).hasSize(2);
        //assertThat(this.ticket.content()).isEqualTo(action.content());
    }

    @Test
    void assign() {
        UUID assignee = UUID.randomUUID();

        this.modificationService.assign(this.ticket, assignee, assignee);

        assertThat(this.ticket.assignee()).hasValue(assignee);
    }

}
