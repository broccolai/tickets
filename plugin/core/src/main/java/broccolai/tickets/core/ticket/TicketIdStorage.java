package broccolai.tickets.core.ticket;

import broccolai.tickets.core.events.EventListener;
import broccolai.tickets.core.events.api.TicketCreationEvent;
import broccolai.tickets.core.storage.SQLQueries;
import net.kyori.event.method.annotation.Subscribe;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class TicketIdStorage implements EventListener {

    private final Map<UUID, Set<Integer>> uniqueIdLinks = new HashMap<>();
    private final Map<TicketStatus, Set<Integer>> ticketStatusLinks = new HashMap<>();

    /**
     * Create a new ticket id storage
     *
     * @param jdbi Jdbi instance
     */
    public TicketIdStorage(final @NonNull Jdbi jdbi) {
        jdbi.useHandle(handle -> {
            handle.createQuery(SQLQueries.SELECT_LIMITED_IDS.get())
                    .mapTo(ValueData.class)
                    .forEach(valueData -> {
                        this.update(valueData.id, valueData.uniqueId, valueData.status);
                    });
        });
    }

    /**
     * Get ids with filters
     *
     * @param uuid     Unique id
     * @param statuses Ticket status array
     * @return List of ids
     */
    public @NonNull List<Integer> getIds(final UUID uuid, final TicketStatus... statuses) {
        Set<Integer> results = new HashSet<>(uniqueIdLinks.get(uuid));

        if (statuses.length == 0) {
            return new ArrayList<>(results);
        }

        Set<Integer> toIntersect = new HashSet<>();

        for (final TicketStatus status : statuses) {
            toIntersect.addAll(ticketStatusLinks.get(status));
        }

        results.retainAll(toIntersect);
        return new ArrayList<>(results);
    }

    public void update(final @NonNull Ticket ticket) {
        this.ticketStatusLinks.values().forEach(set -> set.remove(ticket.getId()));
        this.ticketStatusLinks.get(ticket.getStatus()).add(ticket.getId());
    }

    /**
     * Listener for ticket creations
     *
     * @param event Event
     */
    @Subscribe
    public void onCreate(final @NonNull TicketCreationEvent event) {
        Ticket ticket = event.getTicket();
        this.update(ticket.getId(), ticket.getPlayerUniqueID(), ticket.getStatus());
    }

    private void update(final int id, final @NonNull UUID uniqueId, final @NonNull TicketStatus status) {
        uniqueIdLinks.putIfAbsent(uniqueId, new HashSet<>());
        uniqueIdLinks.get(uniqueId).add(id);
        ticketStatusLinks.putIfAbsent(status, new HashSet<>());
        ticketStatusLinks.get(status).add(id);
    }

    public static final class ValueData {

        private final int id;
        private final UUID uniqueId;
        private final TicketStatus status;

        /**
         * Construct ValueData
         *
         * @param id       Ticket id
         * @param uniqueId Unique id
         * @param status   Ticket status
         */
        public ValueData(final int id, final UUID uniqueId, final TicketStatus status) {
            this.id = id;
            this.uniqueId = uniqueId;
            this.status = status;
        }

    }

}
