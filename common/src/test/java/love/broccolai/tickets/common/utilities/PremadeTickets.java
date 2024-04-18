package love.broccolai.tickets.common.utilities;

import java.time.LocalDateTime;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketType;
import love.broccolai.tickets.api.service.StorageService;

public final class PremadeTickets {
    private PremadeTickets() {
    }

    public static TicketType ticketType() {
        return new TicketType(
            "question",
            "Question",
            "Ask a general question!"
        );
    }

    public static Ticket createTicket(
        final StorageService storageService
    ) {
        LocalDateTime now = LocalDateTime.now();

        return storageService.createTicket(ticketType(), UUID.randomUUID(), "Test " + now);
    }

}
