package broccolai.tickets.storage;

import org.checkerframework.checker.nullness.qual.Nullable;

public enum TimeAmount {
    DAY(86400L),
    WEEK(604800L),
    MONTH(2628000L),
    YEAR(31556952L),
    FOREVER(null);

    private final @Nullable Long length;

    TimeAmount(final @Nullable Long length) {
        this.length = length;
    }

    /**
     * Get the time representation
     *
     * @return a long
     */
    public @Nullable Long getLength() {
        return length;
    }
}
