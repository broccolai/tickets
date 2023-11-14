package love.broccolai.tickets.common.service;

import java.time.Instant;
import java.util.TreeSet;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.model.SimpleTicket;
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
            UUID.randomUUID(),
            TimeUtilities.nowTruncated(),
            new TreeSet<>(Action.SORTER)
        );

        Action openAction = new OpenAction(Instant.now(), UUID.randomUUID(), "Test Message");
        this.ticket.actions().add(openAction);
    }

    @Test
    void close() {
        this.modificationService.close(this.ticket, UUID.randomUUID());
        assertThat(this.ticket.actions().size()).isEqualTo(2);
    }

    // todo: reimplement this when #message is added
    @Test
    @Disabled
    void edit() {
        UUID creator = UUID.randomUUID();
        CommentAction action = this.modificationService.comment(this.ticket, creator, "New message");

        // assertThat(this.ticket.message()).isEqualTo(action.message());
    }

    // todo: reimplement this when #message is added
    @Test
    @Disabled
    void assign() {
        UUID creator = UUID.randomUUID();
        UUID assignee = UUID.randomUUID();

        this.modificationService.assign(this.ticket, creator, assignee);

        // assertThat(this.ticket.assignee()).hasValue(assignee);
    }

}
