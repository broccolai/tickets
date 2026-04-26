package love.broccolai.tickets.common.service;

import com.google.inject.Inject;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.packaged.TicketClosed;
import love.broccolai.tickets.api.service.StatisticService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.api.service.TicketSearch;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CalculatingStatisticService implements StatisticService {

    private static final Instant EARLIEST_SEARCH_DATE = Instant.EPOCH;

    private final StorageService storageService;

    @Inject
    public CalculatingStatisticService(final StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Duration averageTicketsLifespan(final Duration duration) {
        Instant since = this.searchStart(duration);

        Collection<Ticket> closedTickets = this.storageService.findTickets(
            TicketSearch.closed().since(since)
        );

        if (closedTickets.isEmpty()) {
            return Duration.ZERO;
        }

        return Trove.of(closedTickets)
            .mapIfPresent(this::calculateAverageTicketLifespan)
            .average(Duration.ZERO, Duration::plus, Duration::dividedBy);
    }

    private Optional<Duration> calculateAverageTicketLifespan(final Ticket ticket) {
        return Trove.of(ticket.actions())
            .filterIsInstance(TicketClosed.class)
            .last()
            .map(action -> Duration.between(ticket.date(), action.date()));
    }

    private Instant searchStart(final Duration duration) {
        try {
            Instant start = TimeUtilities.nowTruncated().minus(duration);

            if (start.isBefore(EARLIEST_SEARCH_DATE)) {
                return EARLIEST_SEARCH_DATE;
            }

            return start;
        } catch (DateTimeException | ArithmeticException exception) {
            return EARLIEST_SEARCH_DATE;
        }
    }
}
