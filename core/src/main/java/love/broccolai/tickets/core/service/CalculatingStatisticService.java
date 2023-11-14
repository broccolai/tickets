package love.broccolai.tickets.core.service;

import com.google.inject.Inject;
import com.seiama.common.Streams;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.api.service.StatisticService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.core.utilities.TimeUtilities;
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

        Collection<Ticket> closedTickets = this.storageService.findTickets(TicketStatus.CLOSED, since);

        return closedTickets
            .stream()
            .map(this::calculateAverageTicketLifespan)
            .reduce(Duration::plus)
            .map(sum -> sum.dividedBy(closedTickets.size()))
            .orElse(Duration.ZERO);
    }

    private Duration calculateAverageTicketLifespan(final Ticket ticket) {
        //todo: add utility for all of this?
        List<CloseAction> closeActions = Streams.instancesOf(
            ticket.actions().stream(),
            CloseAction.class
        ).toList();

        CloseAction finalCloseAction = closeActions.getLast();

        return Duration.between(ticket.date(), finalCloseAction.date());
    }

}
