package love.broccolai.tickets.core.utilities;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class TimeUtilities {

    private TimeUtilities() {
    }

    public static @NonNull Instant nowTruncated() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

}
