package broccolai.tickets.core.service.interaction;

import broccolai.tickets.api.model.event.impl.TicketConstructionEvent;
import broccolai.tickets.api.model.event.impl.TicketCreateEvent;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.interactions.InteractionService;
import broccolai.tickets.api.service.ticket.TicketService;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class EventInteractionService implements InteractionService {

    private final EventService eventService;
    private final TicketService ticketService;

    @Inject
    public EventInteractionService(
            final @NonNull EventService eventService,
            final @NonNull TicketService ticketService
    ) {
        this.eventService = eventService;
        this.ticketService = ticketService;
    }

    @Override
    public Ticket create(
            final @NonNull PlayerSoul soul,
            final @NonNull MessageInteraction interaction
    ) {
        TicketConstructionEvent constructionEvent = new TicketConstructionEvent(soul, interaction);
        this.eventService.post(constructionEvent);

        if (constructionEvent.cancelled()) {
            return null;
        }

        Ticket ticket = this.ticketService.create(soul, soul.position(), interaction);
        TicketCreateEvent createEvent = new TicketCreateEvent(soul, ticket);
        this.eventService.post(createEvent);

        return ticket;
    }

    @Override
    public void pick() {

    }

}
