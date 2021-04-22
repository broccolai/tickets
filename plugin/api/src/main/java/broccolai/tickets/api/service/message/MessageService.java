package broccolai.tickets.api.service.message;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface MessageService {

    //
    // Sender
    //

    @NonNull Component senderTicketCreation(@NonNull Ticket ticket);

    @NonNull Component senderTicketReopen(@NonNull Ticket ticket);

    @NonNull Component senderTicketUpdate(@NonNull Ticket ticket);

    @NonNull Component senderTicketClose(@NonNull Ticket ticket);

    @NonNull Component senderTicketClaim(@NonNull Ticket ticket);

    @NonNull Component senderTicketUnclaim(@NonNull Ticket ticket);

    @NonNull Component senderTicketAssign(@NonNull Ticket ticket, @NonNull Soul target);

    @NonNull Component senderTicketComplete(@NonNull Ticket ticket);

    @NonNull Component senderTicketNote(@NonNull Ticket ticket);

    //
    // Target
    //

    @NonNull Component targetTicketClaim(@NonNull Ticket ticket, @NonNull Soul soul);

    @NonNull Component targetTicketReopen(@NonNull Ticket ticket, @NonNull Soul soul);

    @NonNull Component targetTicketUnclaim(@NonNull Ticket ticket, @NonNull Soul soul);

    @NonNull Component targetTicketComplete(@NonNull Ticket ticket, @NonNull Soul soul);

    @NonNull Component targetTicketNote(@NonNull Ticket ticket, @NonNull String note, @NonNull Soul soul);

    //
    // Staff
    //

    @NonNull Component staffTicketCreate(@NonNull Ticket ticket);

    @NonNull Component staffTicketUpdate(@NonNull Ticket ticket);

    @NonNull Component staffTicketClose(@NonNull Ticket ticket);

    @NonNull Component staffTicketReopen(@NonNull Ticket ticket, @NonNull Soul soul);

    @NonNull Component staffTicketClaim(@NonNull Ticket ticket, @NonNull Soul soul);

    @NonNull Component staffTicketUnclaim(@NonNull Ticket ticket, @NonNull Soul soul);

    @NonNull Component staffTicketAssign(@NonNull Ticket ticket, @NonNull Soul soul);

    @NonNull Component staffTicketComplete(@NonNull Ticket ticket, @NonNull Soul soul);

    @NonNull Component staffTicketNote(@NonNull Ticket ticket, @NonNull String note, @NonNull Soul soul);

    //
    // Commands
    //

    @NonNull Component commandsTicketList(@NonNull Collection<@NonNull Ticket> tickets);

    @NonNull Component commandsTicketsList(@NonNull Map<@NonNull UUID, @NonNull Collection<@NonNull Ticket>> tickets);

    @NonNull Component commandsTeleport(@NonNull Ticket ticket);

    @NonNull Component commandsHighscore(@NonNull Map<UUID, Integer> ranks);

    @NonNull Component commandsLog(@NonNull Collection<Interaction> interactions);

    //
    // Exception
    //

    @NonNull Component exceptionTicketOpen();

    @NonNull Component exceptionTicketClaimed();

    @NonNull Component exceptionTicketClosed();

    @NonNull Component exceptionTicketNotFound();

    @NonNull Component exceptionTooManyTicketsOpen();

    @NonNull Component exceptionNoPermission();

    @NonNull Component exceptionWrongSender(@NonNull Class<?> sender);

    //
    // Random
    //

    @NonNull Component showTicket(@NonNull Ticket ticket);

    @NonNull Component taskReminder(int count);

}
