package broccolai.tickets.api.service.message;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface MessageService {

    //
    // Sender
    //

    Component senderTicketCreation(@NonNull Ticket ticket);

    Component senderTicketReopen(@NonNull Ticket ticket);

    Component senderTicketUpdate(@NonNull Ticket ticket);

    Component senderTicketClose(@NonNull Ticket ticket);

    Component senderTicketClaim(@NonNull Ticket ticket);

    Component senderTicketUnclaim(@NonNull Ticket ticket);

    Component senderTicketAssign(@NonNull Ticket ticket, @NonNull Soul target);

    Component senderTicketComplete(@NonNull Ticket ticket);

    Component senderTicketNote(@NonNull Ticket ticket);

    //
    // Target
    //

    Component targetTicketClaim(@NonNull Ticket ticket, @NonNull Soul soul);

    Component targetTicketReopen(@NonNull Ticket ticket, @NonNull Soul soul);

    Component targetTicketUnclaim(@NonNull Ticket ticket, @NonNull Soul soul);

    Component targetTicketComplete(@NonNull Ticket ticket, @NonNull Soul soul);

    Component targetTicketNote(@NonNull Ticket ticket, @NonNull String note, @NonNull Soul soul);

    //
    // Staff
    //

    Component staffTicketCreate(@NonNull Ticket ticket);

    Component staffTicketUpdate(@NonNull Ticket ticket);

    Component staffTicketClose(@NonNull Ticket ticket);

    Component staffTicketReopen(@NonNull Ticket ticket, @NonNull Soul soul);

    Component staffTicketClaim(@NonNull Ticket ticket, @NonNull Soul soul);

    Component staffTicketUnclaim(@NonNull Ticket ticket, @NonNull Soul soul);

    Component staffTicketAssign(@NonNull Ticket ticket, @NonNull Soul soul);

    Component staffTicketComplete(@NonNull Ticket ticket, @NonNull Soul soul);

    Component staffTicketNote(@NonNull Ticket ticket, @NonNull String note, @NonNull Soul soul);

    //
    // Commands
    //

    Component commandsTicketList(@NonNull Collection<@NonNull Ticket> tickets);

    Component commandsTicketsList(@NonNull Map<@NonNull UUID, @NonNull Collection<@NonNull Ticket>> tickets);

    Component commandsTeleport(@NonNull Ticket ticket);

    Component commandsHighscore(@NonNull Map<UUID, Integer> ranks);

    Component commandsLog(@NonNull Collection<Interaction> interactions);

    //
    // Exception
    //

    Component exceptionTicketOpen();

    Component exceptionTicketClaimed();

    Component exceptionTicketClosed();

    Component exceptionTicketNotFound();

    Component exceptionTooManyTicketsOpen();

    Component exceptionNoPermission();

    Component exceptionWrongSender(@NonNull Class<?> sender);

    //
    // Random
    //

    Component showTicket(@NonNull Ticket ticket);

    Component taskReminder(int count);

}
