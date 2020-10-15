package broccolai.tickets.locale;

/**
 * Enum representing target types.
 */
public enum TargetType {
    SENDER(true),
    NOTIFICATION(true),
    ANNOUNCEMENT(true),
    DISCORD(false);

    private final boolean hasPrefix;

    TargetType(final boolean hasPrefix) {
        this.hasPrefix = hasPrefix;
    }

    /**
     * Checks if the target type should use a prefix.
     *
     * @return boolean
     */
    public boolean getHasPrefix() {
        return hasPrefix;
    }
}
