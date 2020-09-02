package broccolai.tickets.storage;

import org.jetbrains.annotations.Nullable;

/**
 * Enum of time amounts.
 */
public enum TimeAmount {
    DAY(86400L), WEEK(604800L), MONTH(2628000L), YEAR(31556952L), FOREVER(null);

    @Nullable
    private final Long length;

    TimeAmount(@Nullable Long length) {
        this.length = length;
    }

    /**
     * Get the time representation.
     * @return a long
     */
    @Nullable
    public Long getLength() {
        return length;
    }
}