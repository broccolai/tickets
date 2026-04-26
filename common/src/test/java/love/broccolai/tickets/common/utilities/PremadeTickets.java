package love.broccolai.tickets.common.utilities;

import java.util.List;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.packaged.TicketOpened;
import love.broccolai.tickets.api.model.component.TicketComponents;
import love.broccolai.tickets.api.model.component.TicketType;
import love.broccolai.tickets.api.model.format.TicketFormData;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.format.TicketFormatPart;
import love.broccolai.tickets.api.model.format.TicketFormatStyle;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.model.SimpleTicket;

public final class PremadeTickets {

    private PremadeTickets() {
    }

    public static TicketFormatPart ticketFormatPart() {
        return new TicketFormatPart("message", TicketFormatStyle.Sentence);
    }

    public static TicketFormat ticketType() {
        return new TicketFormat(
            "question",
            "Question",
            "Ask a general question!",
            List.of(
                ticketFormatPart()
            )
        );
    }

    public static TicketFormData ticketForm() {
        TicketFormatPart part = ticketFormatPart();

        return TicketFormData.empty(ticketType().identifier())
            .with(part.identifier(), "What is the meaning of life?");
    }

    public static Ticket ticket() {
        TicketFormat type = ticketType();
        TicketOpened action = new TicketOpened(TimeUtilities.nowTruncated(), UUID.randomUUID(), ticketForm());

        return new SimpleTicket(1, TicketComponents.EMPTY.with(new TicketType(type)), List.of())
            .withAction(action);
    }

    public static Ticket createTicket(final StorageService storageService) {
        return storageService.createTicket(UUID.randomUUID(), ticketType(), ticketForm());
    }
}
