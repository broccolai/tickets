package broccolai.tickets.core.service.ticket;

import broccolai.tickets.api.model.interaction.Interactions;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.Ticket.Status;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.User;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.core.model.interaction.TreeSetInteractions;
import broccolai.tickets.core.model.ticket.TicketImpl;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class StorageTicketService implements TicketService {

    private final StorageService storageService;

    @Inject
    public StorageTicketService(final @NonNull StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public @NonNull Optional<@NonNull Ticket> get(final int id) {
        return Optional.ofNullable(Iterables.getFirst(this.get(Collections.singletonList(id)), null));
    }

    @Override
    public @NonNull Collection<@NonNull Ticket> get(final @NonNull Collection<Integer> queries) {
        Collection<Integer> ids = new HashSet<>(queries);
        Map<Integer, Ticket> tickets = new HashMap<>(this.cache.getAllPresent(ids));
        ids.removeAll(tickets.keySet());

        if (ids.size() != 0) {
            Map<Integer, Ticket> uncached = this.storageService.tickets(ids);
            this.putAllNotPresent(uncached);
        }

        return this.cache.getAllPresent(queries).values();
    }

    @Override
    public @NonNull Collection<@NonNull Ticket> get(
            final @NonNull User user,
            final @NonNull Set<Status> queries
    ) {
        return this.storageService.findTickets(user, queries);
    }

    private void putAllNotPresent(final @NonNull Map<Integer, Ticket> toAdd) {
        toAdd.forEach((id, ticket) -> {
            Ticket current = this.cache.getIfPresent(id);

            if (current == null) {
                this.cache.put(id, ticket);
            }
        });
    }

    private @NonNull Multimap<@NonNull UUID, @NonNull Ticket> filter(final @NonNull Predicate<@NonNull Ticket> predicate) {
        Multimap<UUID, Ticket> tickets = HashMultimap.create();

        for (final Ticket ticket : this.cache.asMap().values()) {
            if (predicate.test(ticket)) {
                tickets.put(ticket.uuid(), ticket);
            }
        }

        return tickets;
    }

}
