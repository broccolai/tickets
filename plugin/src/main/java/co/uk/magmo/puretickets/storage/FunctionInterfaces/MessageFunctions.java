package co.uk.magmo.puretickets.storage.FunctionInterfaces;

import co.uk.magmo.puretickets.ticket.Message;
import co.uk.magmo.puretickets.ticket.Ticket;

import java.util.ArrayList;

public interface MessageFunctions {
    ArrayList<Message> selectAll(Integer id);

    void insert(Ticket ticket, Message message);
}

