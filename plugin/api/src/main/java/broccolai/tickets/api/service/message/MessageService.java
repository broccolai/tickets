package broccolai.tickets.api.service.message;

import broccolai.tickets.api.model.ticket.Ticket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public interface MessageService {

    Component senderTicketCreation(@NonNull Ticket ticket);

    Component senderTicketPick(@NonNull Ticket ticket);

    Component targetTicketPick(@NonNull Ticket ticket);

    Component staffTicketPick(@NonNull Ticket ticket);

    Component commandsTicketList(@NonNull Collection<@NonNull Ticket> tickets);

    Component taskReminder(int count);

}
