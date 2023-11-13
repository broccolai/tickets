package love.broccolai.tickets.api.service;

import java.time.Duration;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface StatisticService {

    Duration averageTicketsLifespan(Duration duration);

}
