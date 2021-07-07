package broccolai.tickets.api.service.message;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.ticket.Ticket;
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

    @NonNull Component commandsHighscore(@NonNull Map<UUID, Integer> ranks);

    @NonNull Component commandsLog(@NonNull Collection<Interaction> interactions);

    //
    // Random
    //

    @NonNull Component showTicket(@NonNull Ticket ticket);

}
