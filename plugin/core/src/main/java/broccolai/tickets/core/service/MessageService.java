package broccolai.tickets.core.service;

import broccolai.tickets.core.ticket.Ticket;

import java.util.Collection;

import net.kyori.adventure.text.Component;

public interface MessageService {

    Component commandsTicketList(Collection<Ticket> tickets);

}
