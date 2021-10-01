package broccolai.tickets.api.model.event;

import broccolai.tickets.api.model.ticket.Ticket;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface TicketEvent extends Event {

    @NonNull Ticket ticket();

}
