package broccolai.tickets.core.utilities;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeUtilities {

    private TimeUtilities() {
    }

    /**
     * Convert a time into a standardised string representation
     *
     * @param time Time to format
     * @return Formatted time string
     */
    public static @NonNull String formatted(final @NonNull LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Convert a minute to a tick
     *
     * @param minute Time value to transition
     * @return Long value
     */
    public static long minuteToLong(final int minute) {
        return minute * 60 * 20;
    }

}
