package love.broccolai.tickets.common.utilities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.format.TicketFormatContent;
import love.broccolai.tickets.api.model.format.TicketFormatPart;
import love.broccolai.tickets.api.model.format.TicketFormatStyle;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.api.utilities.Pair;

public final class PremadeTickets {
    private PremadeTickets() {
    }

    public static TicketFormat ticketType() {
        return new TicketFormat(
            "question",
            "Question",
            "Ask a general question!",
            List.of(
                new TicketFormatPart("message", TicketFormatStyle.Sentence)
            )
        );
    }

    public static TicketFormatContent ticketContent() {
        return TicketFormatContent.of(
            Pair.of("message", "heyy")
        );
    }

    public static Ticket createTicket(
        final StorageService storageService
    ) {
        LocalDateTime now = LocalDateTime.now();

        return storageService.createTicket(UUID.randomUUID(), ticketType(), ticketContent());
    }

}
