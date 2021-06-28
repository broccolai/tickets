package broccolai.tickets.api.service.message;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface OldMessageService {

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
