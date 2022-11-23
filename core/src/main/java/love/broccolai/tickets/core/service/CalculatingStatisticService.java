package love.broccolai.tickets.core.service;

import broccolai.corn.core.Lists;
import com.google.inject.Inject;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.CloseAction;
import love.broccolai.tickets.api.service.StatisticService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.core.utilities.TimeUtilities;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;

public final class CalculatingStatisticService implements StatisticService {

    private final StorageService storageService;

    @Inject
    public CalculatingStatisticService(final @NonNull StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Duration averageTicketsLifespan(final @NonNull Duration duration) {
        Instant since = TimeUtilities.nowTruncated().minus(duration);

        Collection<Ticket> closedTickets = this.storageService.findTickets(TicketStatus.CLOSED, null, since);
        Collection<Duration> ticketLifespans = Lists.map(closedTickets, this::calculateAverageTicketLifespan);

        Duration result = Duration.ZERO;

        for (final Duration lifespan : ticketLifespans) {
            result = result.plus(lifespan);
        }

        return result.dividedBy(ticketLifespans.size());
    }

    private Duration calculateAverageTicketLifespan(final @NonNull Ticket ticket) {
        Action closeAction = Lists.last(
                ticket.actions(),
                action -> action instanceof CloseAction
        );

        return Duration.between(ticket.date(), closeAction.date());
    }

}
