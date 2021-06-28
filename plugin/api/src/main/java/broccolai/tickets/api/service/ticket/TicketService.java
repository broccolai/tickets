package broccolai.tickets.api.service.ticket;

import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.Soul;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface TicketService {

    @NonNull Ticket create(@NonNull Soul soul, @NonNull MessageInteraction interaction);

    @NonNull Optional<Ticket> get(int id);

    @NonNull Collection<@NonNull Ticket> get(@NonNull Collection<Integer> ids);

    @NonNull Multimap<@NonNull UUID, @NonNull Ticket> get(@NonNull Set<TicketStatus> statuses);

    @NonNull Collection<@NonNull Ticket> get(@NonNull Soul soul, @NonNull Set<TicketStatus> statuses);

}
