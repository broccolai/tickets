package broccolai.tickets.utilities;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilities for time
 */
public final class TimeUtilities {

    private TimeUtilities() {
    }

    /**
     * Convert a time into a standardised string representation
     *
     * @param time the time to format
     * @return a formatted time string
     */
    @NonNull
    public static String formatted(@NonNull final LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Convert a minute to a tick
     *
     * @param minute time value to transition
     * @return long value
     */
    public static long minuteToLong(final int minute) {
        return minute * 60 * 20;
    }

}
