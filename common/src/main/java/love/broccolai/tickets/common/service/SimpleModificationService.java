package love.broccolai.tickets.common.service;

import com.google.inject.Inject;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.packaged.TicketAssigned;
import love.broccolai.tickets.api.model.action.packaged.TicketClosed;
import love.broccolai.tickets.api.model.action.packaged.TicketCommented;
import love.broccolai.tickets.api.model.action.packaged.TicketComponentAttached;
import love.broccolai.tickets.api.model.action.packaged.TicketComponentDetached;
import love.broccolai.tickets.api.model.action.packaged.TicketReopened;
import love.broccolai.tickets.api.model.action.packaged.TicketUnassigned;
import love.broccolai.tickets.api.model.component.ComponentKey;
import love.broccolai.tickets.api.model.component.TicketComponent;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SimpleModificationService implements ModificationService {

    private final StorageService storageService;

    @Inject
    public SimpleModificationService(final StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public TicketClosed close(
        final Ticket ticket,
        final UUID creator
    ) {
        TicketClosed action = new TicketClosed(TimeUtilities.nowTruncated(), creator);
        this.storageService.appendAction(ticket, action);

        return action;
    }

    @Override
    public TicketReopened reopen(
        final Ticket ticket,
        final UUID creator
    ) {
        TicketReopened action = new TicketReopened(TimeUtilities.nowTruncated(), creator);
        this.storageService.appendAction(ticket, action);

        return action;
    }

    @Override
    public TicketCommented comment(
        final Ticket ticket,
        final UUID creator,
        final String message
    ) {
        TicketCommented action = new TicketCommented(TimeUtilities.nowTruncated(), creator, message);
        this.storageService.appendAction(ticket, action);

        return action;
    }

    @Override
    public TicketAssigned assign(
        final Ticket ticket,
        final UUID creator,
        final UUID assignee
    ) {
        TicketAssigned action = new TicketAssigned(TimeUtilities.nowTruncated(), creator, assignee);
        this.storageService.appendAction(ticket, action);

        return action;
    }

    @Override
    public TicketUnassigned unassign(
        final Ticket ticket,
        final UUID creator
    ) {
        TicketUnassigned action = new TicketUnassigned(TimeUtilities.nowTruncated(), creator);
        this.storageService.appendAction(ticket, action);

        return action;
    }

    @Override
    public TicketComponentAttached attach(
        final Ticket ticket,
        final UUID creator,
        final TicketComponent component
    ) {
        TicketComponentAttached action = new TicketComponentAttached(TimeUtilities.nowTruncated(), creator, component);
        this.storageService.appendAction(ticket, action);

        return action;
    }

    @Override
    public TicketComponentDetached detach(
        final Ticket ticket,
        final UUID creator,
        final ComponentKey<? extends TicketComponent> key
    ) {
        TicketComponentDetached action = new TicketComponentDetached(TimeUtilities.nowTruncated(), creator, key);
        this.storageService.appendAction(ticket, action);

        return action;
    }
}
