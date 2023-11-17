package love.broccolai.tickets.api.model.proflie;

import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record Profile(
    UUID uuid
) {

}
