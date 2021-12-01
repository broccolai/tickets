package broccolai.tickets.api.service.ticket;

import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.Ticket.Status;
import broccolai.tickets.api.model.user.User;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface TicketService {

    @NonNull Optional<Ticket> get(int id);

    @NonNull Multimap<@NonNull UUID, @NonNull Ticket> get(@NonNull Set<Status> statuses);

    @NonNull Collection<@NonNull Ticket> get(@NonNull User user, @NonNull Set<Status> statuses);

}
