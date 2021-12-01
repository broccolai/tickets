package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Action {

    @NonNull UUID creator();

    @NonNull Instant creationDate();

}
