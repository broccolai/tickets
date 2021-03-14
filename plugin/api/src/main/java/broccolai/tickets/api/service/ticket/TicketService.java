package broccolai.tickets.api.service.ticket;

import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.Soul;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface TicketService {

    @NonNull Ticket create(@NonNull Soul soul, @NonNull Position position, @NonNull MessageInteraction interaction);

    @NonNull Optional<Ticket> get(int id);

    @NonNull Collection<@NonNull Ticket> get(@NonNull Collection<Integer> ids);

    @NonNull Collection<@NonNull Ticket> get(@NonNull Soul soul, @NonNull Set<TicketStatus> statuses);

    int count(@NonNull Set<TicketStatus> statuses);

    void queue(@NonNull Ticket ticket);

    Collection<Ticket> queued();

}
