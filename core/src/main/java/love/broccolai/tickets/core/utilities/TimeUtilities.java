package love.broccolai.tickets.core.utilities;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TimeUtilities {

    private TimeUtilities() {
    }

    public static Instant nowTruncated() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

}
