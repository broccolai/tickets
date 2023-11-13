package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.UUID;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public interface Action {

    Instant date();

    UUID creator();

}
