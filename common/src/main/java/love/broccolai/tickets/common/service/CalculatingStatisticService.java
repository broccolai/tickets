package love.broccolai.tickets.common.service;

import com.google.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.api.service.StatisticService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CalculatingStatisticService implements StatisticService {

    private final StorageService storageService;

    @Inject
    public CalculatingStatisticService(final StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Duration averageTicketsLifespan(final Duration duration) {
        Instant since = TimeUtilities.nowTruncated().minus(duration);

        Collection<Ticket> closedTickets = this.storageService.findTickets(EnumSet.of(TicketStatus.CLOSED), null, since);

        return Trove.of(closedTickets)
            .mapIfPresent(this::calculateAverageTicketLifespan)
            .average(Duration.ZERO, Duration::plus, Duration::dividedBy);
    }

    private Optional<Duration> calculateAverageTicketLifespan(final Ticket ticket) {
        return Trove.of(ticket.actions())
            .filterIsInstance(CloseAction.class)
            .last()
            .map(action -> Duration.between(ticket.date(), action.date()));
    }

}
