package broccolai.tickets.utilities.generic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtilities {
    public static String formatted(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static Long minuteToLong(Integer minute) {
        return (long) (minute * 60 * 20);
    }
}
