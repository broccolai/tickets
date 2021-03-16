package broccolai.tickets.api.service.message;

import broccolai.tickets.api.model.ticket.Ticket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public interface MessageService {

    Component senderTicketCreation(@NonNull Ticket ticket);

    Component senderTicketReopen(@NonNull Ticket ticket);

    Component senderTicketUpdate(@NonNull Ticket ticket);

    Component senderTicketClose(@NonNull Ticket ticket);

    Component senderTicketClaim(@NonNull Ticket ticket);

    Component senderTicketUnclaim(@NonNull Ticket ticket);

    Component senderTicketAssign(@NonNull Ticket ticket);

    Component senderTicketComplete(@NonNull Ticket ticket);

    Component senderTicketNote(@NonNull Ticket ticket);

    Component targetTicketClaim(@NonNull Ticket ticket);

    Component targetTicketReopen(@NonNull Ticket ticket);

    Component targetTicketUnclaim(@NonNull Ticket ticket);

    Component targetTicketComplete(@NonNull Ticket ticket);

    Component targetTicketNote(@NonNull Ticket ticket);

    Component staffTicketCreate(@NonNull Ticket ticket);

    Component staffTicketReopen(@NonNull Ticket ticket);

    Component staffTicketUpdate(@NonNull Ticket ticket);

    Component staffTicketClose(@NonNull Ticket ticket);

    Component staffTicketClaim(@NonNull Ticket ticket);

    Component staffTicketUnclaim(@NonNull Ticket ticket);

    Component staffTicketAssign(@NonNull Ticket ticket);

    Component staffTicketComplete(@NonNull Ticket ticket);

    Component staffTicketNote(@NonNull Ticket ticket);

    Component commandsTicketList(@NonNull Collection<@NonNull Ticket> tickets);

    Component showTicket(@NonNull Ticket ticket);

    Component taskReminder(int count);

}
