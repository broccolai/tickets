package broccolai.tickets.api.model.event;

import broccolai.tickets.api.model.ticket.Ticket;

public interface TicketEvent extends Event {

    Ticket ticket();

}
