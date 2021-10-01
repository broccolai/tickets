package broccolai.tickets.core.service.ticket;

import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.core.model.ticket.TicketImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
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
public final class CachedTicketService implements TicketService {

    private final StorageService storageService;

    private final Cache<@NonNull Integer, @NonNull Ticket> cache = Caffeine.newBuilder().build();
    private final Multimap<UUID, TicketStatus> lookups = MultimapBuilder.hashKeys().enumSetValues(TicketStatus.class).build();

    @Inject
    public CachedTicketService(final @NonNull StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public @NonNull Ticket create(
            final @NonNull Soul soul,
            final @NonNull MessageInteraction interaction
    ) {
        int id = this.storageService.create(soul, interaction);
        Ticket ticket = new TicketImpl(id, soul.uuid(), TicketStatus.OPEN, null);
        ticket.interactions().add(interaction);
        this.cache.put(id, ticket);

        return ticket;
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
    public @NonNull Multimap<@NonNull UUID, @NonNull Ticket> get(final @NonNull Set<TicketStatus> queries) {
        Set<TicketStatus> modifiableQueries = new HashSet<>(queries);

        for (final TicketStatus query : queries) {
            if (!this.lookups.containsEntry(null, query)) {
                modifiableQueries.add(query);
            }
        }

        if (!modifiableQueries.isEmpty()) {
            this.putAllNotPresent(this.storageService.findTickets(modifiableQueries));
            this.lookups.get(null).addAll(modifiableQueries);
        }

        return this.filter(ticket -> queries.contains(ticket.status()));
    }

    @Override
    public @NonNull Collection<@NonNull Ticket> get(
            final @NonNull Soul soul,
            final @NonNull Set<TicketStatus> queries
    ) {
        Set<TicketStatus> modifiableQueries = new HashSet<>(queries);

        if (this.lookups.containsKey(soul.uuid())) {
            Collection<TicketStatus> loaded = this.lookups.get(soul.uuid());
            modifiableQueries.removeAll(loaded);
        }

        if (!modifiableQueries.isEmpty()) {
            this.putAllNotPresent(this.storageService.findTickets(soul, modifiableQueries));
            this.lookups.get(soul.uuid()).addAll(modifiableQueries);
        }

        return this.filter(ticket -> {
            return ticket.player().equals(soul.uuid()) && queries.contains(ticket.status());
        }).get(soul.uuid());
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
                tickets.put(ticket.player(), ticket);
            }
        }

        return tickets;
    }

}
