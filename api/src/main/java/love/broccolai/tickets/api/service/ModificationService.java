package love.broccolai.tickets.api.service;

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
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ModificationService {

    TicketClosed close(Ticket ticket, UUID creator);

    TicketReopened reopen(Ticket ticket, UUID creator);

    TicketCommented comment(Ticket ticket, UUID creator, String message);

    TicketAssigned assign(Ticket ticket, UUID creator, UUID assignee);

    TicketUnassigned unassign(Ticket ticket, UUID creator);

    TicketComponentAttached attach(Ticket ticket, UUID creator, TicketComponent component);

    TicketComponentDetached detach(Ticket ticket, UUID creator, ComponentKey<? extends TicketComponent> key);
}
