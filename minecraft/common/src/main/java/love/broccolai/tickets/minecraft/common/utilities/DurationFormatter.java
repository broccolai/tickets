package love.broccolai.tickets.minecraft.common.utilities;

import java.time.Duration;

public final class DurationFormatter {

    private DurationFormatter() {
    }

    /**
     * Formats a Duration object into a string of format "Xh Ym".
     *
     * @param duration The Duration object to format.
     * @return A formatted string representing the duration in hours and minutes.
     */
    public static String formatDuration(final Duration duration) {
        if (duration.isZero()) {
            return "unknown";
        }

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m");
        }
        return sb.toString().trim();
    }
}
