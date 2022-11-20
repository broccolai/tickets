package love.broccolai.tickets.api.service;

import java.time.Duration;

public interface StatisticService {

    Duration averageTicketsLifespan(Duration duration);

}
