package broccolai.tickets.core.service.interaction;

import broccolai.tickets.api.model.event.impl.TicketCloseEvent;
import broccolai.tickets.api.model.event.impl.TicketCompleteEvent;
import broccolai.tickets.api.model.event.impl.TicketConstructionEvent;
import broccolai.tickets.api.model.event.impl.TicketCreateEvent;
import broccolai.tickets.api.model.event.impl.TicketClaimEvent;
import broccolai.tickets.api.model.event.impl.TicketUpdateEvent;
import broccolai.tickets.api.model.interaction.Action;
import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.interactions.InteractionService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.core.model.interaction.BasicInteraction;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.time.LocalDateTime;

import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class EventInteractionService implements InteractionService {

    private final StorageService storageService;
    private final EventService eventService;
    private final TicketService ticketService;

    @Inject
    public EventInteractionService(
            final @NonNull StorageService storageService,
            final @NonNull EventService eventService,
            final @NonNull TicketService ticketService
    ) {
        this.storageService = storageService;
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
    public void update(
            final @NonNull PlayerSoul soul,
            final @NonNull Ticket ticket,
            final @NonNull MessageInteraction interaction
    ) {
        ticket.message(interaction);

        this.storageService.queue(ticket, interaction);
        TicketUpdateEvent event = new TicketUpdateEvent(soul, ticket);
        this.eventService.post(event);
    }

    @Override
    public void close(@NonNull final PlayerSoul soul, @NonNull final Ticket ticket) {
        Interaction interaction = new BasicInteraction(Action.CLOSE, LocalDateTime.now(), soul.uuid());
        ticket.status(TicketStatus.CLOSED);

        this.storageService.queue(ticket, interaction);
        TicketCloseEvent event = new TicketCloseEvent(soul, ticket);
        this.eventService.post(event);
    }

    @Override
    public void claim(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        Interaction interaction = new BasicInteraction(Action.CLAIM, LocalDateTime.now(), soul.uuid());
        ticket.status(TicketStatus.CLAIMED);
        ticket.claimer(soul.uuid());

        this.storageService.queue(ticket, interaction);
        TicketClaimEvent event = new TicketClaimEvent(soul, ticket);
        this.eventService.post(event);
    }

    @Override
    public void complete(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        Interaction interaction = new BasicInteraction(Action.CLAIM, LocalDateTime.now(), soul.uuid());
        ticket.status(TicketStatus.CLOSED);
        ticket.claimer(soul.uuid());

        this.storageService.queue(ticket, interaction);
        TicketCompleteEvent event = new TicketCompleteEvent(soul, ticket);
        this.eventService.post(event);
    }

}
