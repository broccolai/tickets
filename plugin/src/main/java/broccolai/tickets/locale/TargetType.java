package broccolai.tickets.locale;

public enum TargetType {
    SENDER(true), NOTIFICATION(true), ANNOUNCEMENT(true), DISCORD(false);

    private final Boolean hasPrefix;

    TargetType(Boolean hasPrefix) {
        this.hasPrefix = hasPrefix;
    }

    public Boolean getHasPrefix() {
        return hasPrefix;
    }
}