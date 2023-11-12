package love.broccolai.tickets.core.service;

import static com.google.common.truth.Truth.assertThat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.CloseAction;
import love.broccolai.tickets.api.service.StatisticService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.core.utilities.TicketsH2Extension;
import love.broccolai.tickets.core.utilities.TimeUtilities;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class CalculatingStatisticServiceTest {

    @RegisterExtension
    JdbiExtension h2Extension = TicketsH2Extension.instance();

    private StorageService storageService;
    private StatisticService statisticService;

    @BeforeEach
    void setupEach() {
        this.storageService = new DatabaseStorageService(this.h2Extension.getJdbi());
        this.statisticService = new CalculatingStatisticService(this.storageService);
    }

    @Test
    void averageTicketsLifespan() {
        Instant fiveMinutes = TimeUtilities.nowTruncated().plus(5, ChronoUnit.MINUTES);
        Action close = new CloseAction(fiveMinutes, UUID.randomUUID(), "");

        Ticket ticket1 = this.storageService.createTicket(UUID.randomUUID(), "");
        Ticket ticket2 = this.storageService.createTicket(UUID.randomUUID(), "");

        ticket1.status(TicketStatus.CLOSED);
        ticket1.actions().add(close);

        ticket2.status(TicketStatus.CLOSED);
        ticket2.actions().add(close);

        this.storageService.saveTicket(ticket1);
        this.storageService.saveTicket(ticket2);

        Duration duration = this.statisticService.averageTicketsLifespan(Duration.ofHours(1));
        assertThat(duration).isGreaterThan(Duration.ofMinutes(4));
        assertThat(duration).isLessThan(Duration.ofMinutes(6));
    }

}
