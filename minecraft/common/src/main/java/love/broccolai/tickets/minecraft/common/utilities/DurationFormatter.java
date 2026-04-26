package love.broccolai.tickets.minecraft.common.utilities;

import java.time.Duration;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DurationFormatter {

    private DurationFormatter() {
    }

    public static String formatDuration(final Duration duration) {
        if (duration.isZero()) {
            return "unknown";
        }

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        StringBuilder formattedDuration = new StringBuilder();

        if (hours == 0 && minutes == 0) {
            return "<1m";
        }

        if (hours > 0) {
            formattedDuration.append(hours).append("h ");
        }

        if (minutes > 0) {
            formattedDuration.append(minutes).append("m");
        }

        return formattedDuration.toString().trim();
    }
}
