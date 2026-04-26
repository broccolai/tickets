package love.broccolai.tickets.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.model.action.packaged.TicketAssigned;
import love.broccolai.tickets.api.model.action.packaged.TicketClosed;
import love.broccolai.tickets.api.model.action.packaged.TicketCommented;
import love.broccolai.tickets.api.model.action.packaged.TicketComponentAttached;
import love.broccolai.tickets.api.model.action.packaged.TicketComponentDetached;
import love.broccolai.tickets.api.model.action.packaged.TicketOpened;
import love.broccolai.tickets.api.model.component.ComponentKey;
import love.broccolai.tickets.api.model.component.TicketComponent;
import love.broccolai.tickets.api.model.component.TicketComponents;
import love.broccolai.tickets.api.model.component.TicketType;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.model.SimpleTicket;
import love.broccolai.tickets.common.utilities.PremadeTickets;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class SimpleModificationServiceTest {

    private final StorageService storageService = mock(StorageService.class);
    private final ModificationService modificationService = new SimpleModificationService(this.storageService);

    private SimpleTicket ticket;

    @BeforeEach
    void setup() {
        this.ticket = new SimpleTicket(
            1,
            TicketComponents.EMPTY.with(new TicketType(PremadeTickets.ticketType())),
            new ArrayList<>()
        );

        TicketAction openAction = new TicketOpened(
            TimeUtilities.nowTruncated(),
            UUID.randomUUID(),
            PremadeTickets.ticketForm()
        );

        this.ticket = (SimpleTicket) this.ticket.withAction(openAction);
    }

    @Test
    void close() {
        TicketAction action = this.modificationService.close(this.ticket, UUID.randomUUID());
        SimpleTicket updatedTicket = (SimpleTicket) this.ticket.withAction(action);

        assertThat(updatedTicket.actions()).hasSize(2);
        assertThat(updatedTicket.status()).isEqualTo(TicketStatus.CLOSED);
    }

    @Test
    void reopen() {
        TicketClosed close = new TicketClosed(TimeUtilities.nowTruncated(), UUID.randomUUID());
        SimpleTicket closedTicket = (SimpleTicket) this.ticket.withAction(close);

        TicketAction action = this.modificationService.reopen(closedTicket, UUID.randomUUID());
        SimpleTicket updatedTicket = (SimpleTicket) closedTicket.withAction(action);

        assertThat(updatedTicket.status()).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    void comment() {
        TicketCommented action = this.modificationService.comment(
            this.ticket,
            UUID.randomUUID(),
            "New message"
        );

        List<TicketAction> actions = this.ticket.withAction(action).actions()
            .stream()
            .toList();

        assertThat(actions).hasSize(2);
        assertThat(actions).contains(action);
        assertThrows(UnsupportedOperationException.class, () -> actions.add(action));
    }

    @Test
    void assign() {
        UUID assignee = UUID.randomUUID();

        TicketAction action = this.modificationService.assign(this.ticket, assignee, assignee);
        SimpleTicket updatedTicket = (SimpleTicket) this.ticket.withAction(action);

        assertThat(updatedTicket.assignee()).hasValue(assignee);
    }

    @Test
    void unassign() {
        UUID assignee = UUID.randomUUID();
        TicketAssigned assign = new TicketAssigned(TimeUtilities.nowTruncated(), UUID.randomUUID(), assignee);
        SimpleTicket assignedTicket = (SimpleTicket) this.ticket.withAction(assign);

        TicketAction action = this.modificationService.unassign(assignedTicket, UUID.randomUUID());
        SimpleTicket updatedTicket = (SimpleTicket) assignedTicket.withAction(action);

        assertThat(updatedTicket.status()).isEqualTo(TicketStatus.OPEN);
        assertThat(updatedTicket.assignee()).isEmpty();
    }

    @Test
    void attachComponent() {
        TicketPriority priority = new TicketPriority(3);

        TicketComponentAttached action = this.modificationService.attach(this.ticket, UUID.randomUUID(), priority);
        SimpleTicket updatedTicket = (SimpleTicket) this.ticket.withAction(action);

        assertThat(updatedTicket.components().find(TicketPriority.KEY)).hasValue(priority);
    }

    @Test
    void detachComponent() {
        TicketPriority priority = new TicketPriority(3);
        SimpleTicket ticket = (SimpleTicket) this.ticket.withAction(
            new TicketComponentAttached(TimeUtilities.nowTruncated(), UUID.randomUUID(), priority)
        );

        TicketComponentDetached action = this.modificationService.detach(ticket, UUID.randomUUID(), TicketPriority.KEY);
        SimpleTicket updatedTicket = (SimpleTicket) ticket.withAction(action);

        assertThat(updatedTicket.components().find(TicketPriority.KEY)).isEmpty();
    }

    private record TicketPriority(
        int level
    ) implements TicketComponent {

        private static final ComponentKey<TicketPriority> KEY = ComponentKey.of(
            "test:priority",
            TicketPriority.class
        );

        @Override
        public ComponentKey<TicketPriority> key() {
            return KEY;
        }
    }
}
