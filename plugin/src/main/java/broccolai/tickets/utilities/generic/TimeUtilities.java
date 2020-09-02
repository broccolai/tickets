package broccolai.tickets.utilities.generic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jetbrains.annotations.NotNull;

/**
 * Utilities for Time.
 */
public class TimeUtilities {
    /**
     * Convert a time into a standardised string representation.
     * @param time the time to format
     * @return a formatted time string
     */
    @NotNull
    public static String formatted(@NotNull LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Convert a minute to a tick.
     * @param minute time value to transition
     * @return long value
     */
    public static long minuteToLong(int minute) {
        return minute * 60 * 20;
    }
}
