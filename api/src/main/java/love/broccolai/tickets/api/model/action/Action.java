package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.Comparator;
import java.util.UUID;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public interface Action {

    Comparator<Action> SORTER = Comparator.comparing(Action::date);

    Instant date();

    UUID creator();

}
