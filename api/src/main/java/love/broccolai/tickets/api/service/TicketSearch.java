package love.broccolai.tickets.api.service;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record TicketSearch(
    Set<TicketStatus> statuses,
    @Nullable UUID creator,
    @Nullable Instant since
) {

    public TicketSearch {
        statuses = Set.copyOf(statuses);
    }

    public static TicketSearch matching(final Set<TicketStatus> statuses) {
        return new TicketSearch(statuses, null, null);
    }

    public static TicketSearch open() {
        return TicketSearch.matching(Set.of(TicketStatus.OPEN));
    }

    public static TicketSearch picked() {
        return TicketSearch.matching(Set.of(TicketStatus.PICKED));
    }

    public static TicketSearch closed() {
        return TicketSearch.matching(Set.of(TicketStatus.CLOSED));
    }

    public static TicketSearch allActive() {
        return TicketSearch.matching(EnumSet.of(TicketStatus.OPEN, TicketStatus.PICKED));
    }

    public static TicketSearch all() {
        return TicketSearch.matching(EnumSet.allOf(TicketStatus.class));
    }

    public TicketSearch createdBy(final UUID creator) {
        return new TicketSearch(this.statuses, creator, this.since);
    }

    public TicketSearch since(final Instant since) {
        return new TicketSearch(this.statuses, this.creator, since);
    }
}
