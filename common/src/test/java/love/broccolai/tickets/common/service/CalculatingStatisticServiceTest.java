package love.broccolai.tickets.common.service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.api.service.StatisticService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import love.broccolai.tickets.common.serialization.jdbi.ActionMapper;
import love.broccolai.tickets.common.utilities.PremadeActionRegistry;
import love.broccolai.tickets.common.utilities.PremadeTickets;
import love.broccolai.tickets.common.utilities.TicketsH2Extension;
import love.broccolai.tickets.common.utilities.TimeUtilities;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.google.common.truth.Truth.assertThat;

class CalculatingStatisticServiceTest {

    @RegisterExtension
    JdbiExtension h2Extension = TicketsH2Extension.instance();

    private StorageService storageService;
    private StatisticService statisticService;

    @BeforeEach
    void setupEach() {
        this.storageService = new DatabaseStorageService(this.h2Extension.getJdbi(), new ActionMapper(PremadeActionRegistry.create()), new DatabaseConfiguration());
        this.statisticService = new CalculatingStatisticService(this.storageService);
    }

    @Test
    void averageTicketsLifespan() {
        Instant fiveMinutes = TimeUtilities.nowTruncated().plus(5, ChronoUnit.MINUTES);
        Action close = new CloseAction(fiveMinutes, UUID.randomUUID());

        Ticket ticket1 = this.storageService.createTicket(PremadeTickets.ticketType(), UUID.randomUUID(), "");
        Ticket ticket2 = this.storageService.createTicket(PremadeTickets.ticketType(), UUID.randomUUID(), "");

        ticket1.actions().add(close);
        ticket2.actions().add(close);

        this.storageService.saveAction(ticket1, close);
        this.storageService.saveAction(ticket2, close);

        Duration duration = this.statisticService.averageTicketsLifespan(Duration.ofHours(1));
        assertThat(duration).isGreaterThan(Duration.ofMinutes(4));
        assertThat(duration).isLessThan(Duration.ofMinutes(6));
    }

}
