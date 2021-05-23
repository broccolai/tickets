package broccolai.tickets.core.service.interaction;

import broccolai.tickets.api.model.event.impl.TicketAssignEvent;
import broccolai.tickets.api.model.event.impl.TicketClaimEvent;
import broccolai.tickets.api.model.event.impl.TicketCloseEvent;
import broccolai.tickets.api.model.event.impl.TicketCompleteEvent;
import broccolai.tickets.api.model.event.impl.TicketConstructionEvent;
import broccolai.tickets.api.model.event.impl.TicketCreateEvent;
import broccolai.tickets.api.model.event.impl.TicketNoteEvent;
import broccolai.tickets.api.model.event.impl.TicketReopenEvent;
import broccolai.tickets.api.model.event.impl.TicketUnclaimEvent;
import broccolai.tickets.api.model.event.impl.TicketUpdateEvent;
import broccolai.tickets.api.model.interaction.Action;
import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.interactions.InteractionService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.core.configuration.MainConfiguration;
import broccolai.tickets.core.exceptions.TicketClaimed;
import broccolai.tickets.core.exceptions.TicketClosed;
import broccolai.tickets.core.exceptions.TicketOpen;
import broccolai.tickets.core.exceptions.TooManyOpenTickets;
import broccolai.tickets.core.model.interaction.BasicInteraction;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.EnumSet;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class EventInteractionService implements InteractionService {

    private final StorageService storageService;
    private final EventService eventService;
    private final TicketService ticketService;
    private final int openTicketLimit;

    @Inject
    public EventInteractionService(
            final @NonNull MainConfiguration mainConfiguration,
            final @NonNull StorageService storageService,
            final @NonNull EventService eventService,
            final @NonNull TicketService ticketService
    ) {
        this.storageService = storageService;
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.openTicketLimit = mainConfiguration.advancedConfiguration.openTicketLimit;
    }

    @Override
    public @NonNull Ticket create(
            final @NonNull PlayerSoul soul,
            final @NonNull MessageInteraction interaction
    ) {
        TicketConstructionEvent constructionEvent = new TicketConstructionEvent(soul, interaction);
        this.eventService.post(constructionEvent);

        if (constructionEvent.cancelled()) {
            return null;
        }

        int currentOpen = this.ticketService.get(soul, EnumSet.of(TicketStatus.OPEN)).size();

        if (currentOpen >= this.openTicketLimit) {
            throw new TooManyOpenTickets();
        }

        Ticket ticket = this.ticketService.create(soul, interaction);
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
        this.requireOpen(ticket);

        ticket.message(interaction);

        this.storageService.queue(ticket, interaction);
        TicketUpdateEvent event = new TicketUpdateEvent(soul, ticket);
        this.eventService.post(event);
    }

    @Override
    public void close(final @NonNull PlayerSoul soul, final @NonNull Ticket ticket) {
        this.requireOpen(ticket);

        Interaction interaction = new BasicInteraction(Action.CLOSE, LocalDateTime.now(ZoneId.systemDefault()), soul.uuid());
        ticket.status(TicketStatus.CLOSED);

        this.storageService.queue(ticket, interaction);
        TicketCloseEvent event = new TicketCloseEvent(soul, ticket);
        this.eventService.post(event);
    }

    @Override
    public void claim(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        this.requireOpenAndUnclaimed(ticket);

        Interaction interaction = new BasicInteraction(Action.CLAIM, LocalDateTime.now(ZoneId.systemDefault()), soul.uuid());
        ticket.status(TicketStatus.CLAIMED);
        ticket.claimer(soul.uuid());

        this.storageService.queue(ticket, interaction);
        TicketClaimEvent event = new TicketClaimEvent(soul, ticket);
        this.eventService.post(event);
    }

    @Override
    public void complete(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        this.requireOpen(ticket);

        Interaction interaction = new BasicInteraction(Action.COMPLETE, LocalDateTime.now(ZoneId.systemDefault()), soul.uuid());
        ticket.status(TicketStatus.CLOSED);
        ticket.claimer(soul.uuid());

        this.storageService.queue(ticket, interaction);
        TicketCompleteEvent event = new TicketCompleteEvent(soul, ticket);
        this.eventService.post(event);
    }

    @Override
    public void assign(final @NonNull OnlineSoul soul, final @NonNull Soul target, final @NonNull Ticket ticket) {
        this.requireOpenAndUnclaimed(ticket);

        Interaction interaction = new BasicInteraction(Action.ASSIGN, LocalDateTime.now(ZoneId.systemDefault()), soul.uuid());
        ticket.status(TicketStatus.CLAIMED);
        ticket.claimer(soul.uuid());

        this.storageService.queue(ticket, interaction);
        TicketAssignEvent event = new TicketAssignEvent(soul, target, ticket);
        this.eventService.post(event);
    }

    @Override
    public void unclaim(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        this.requireOpen(ticket);

        Interaction interaction = new BasicInteraction(Action.UNCLAIM, LocalDateTime.now(ZoneId.systemDefault()), soul.uuid());
        ticket.status(TicketStatus.OPEN);
        ticket.claimer(null);

        this.storageService.queue(ticket, interaction);
        TicketUnclaimEvent event = new TicketUnclaimEvent(soul, ticket);
        this.eventService.post(event);
    }

    @Override
    public void reopen(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        this.requireClosed(ticket);

        Interaction interaction = new BasicInteraction(Action.REOPEN, LocalDateTime.now(ZoneId.systemDefault()), soul.uuid());
        ticket.status(TicketStatus.OPEN);

        this.storageService.queue(ticket, interaction);
        TicketReopenEvent event = new TicketReopenEvent(soul, ticket);
        this.eventService.post(event);
    }

    @Override
    public void note(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket, final @NonNull MessageInteraction message) {
        Interaction interaction = new BasicInteraction(Action.NOTE, LocalDateTime.now(ZoneId.systemDefault()), soul.uuid());

        this.storageService.queue(ticket, interaction);
        TicketNoteEvent event = new TicketNoteEvent(soul, ticket, message.message());
        this.eventService.post(event);
    }

    private void requireOpen(final @NonNull Ticket ticket) {
        if (ticket.status() == TicketStatus.CLOSED) {
            throw new TicketClosed();
        }
    }

    private void requireClosed(final @NonNull Ticket ticket) {
        if (ticket.status() != TicketStatus.CLOSED) {
            throw new TicketOpen();
        }
    }

    private void requireOpenAndUnclaimed(final @NonNull Ticket ticket) {
        this.requireOpen(ticket);

        if (ticket.status() == TicketStatus.CLAIMED) {
            throw new TicketClaimed();
        }
    }

}
