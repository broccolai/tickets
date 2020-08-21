package broccolai.tickets.locale;

public enum MessageNames {
    NEW_TICKET(TargetType.SENDER, TargetType.ANNOUNCEMENT, TargetType.DISCORD),
    UPDATE_TICKET(TargetType.SENDER, TargetType.ANNOUNCEMENT, TargetType.DISCORD),
    CLOSE_TICKET(TargetType.SENDER, TargetType.ANNOUNCEMENT, TargetType.DISCORD),
    PICK_TICKET(TargetType.SENDER, TargetType.NOTIFICATION, TargetType.ANNOUNCEMENT, TargetType.DISCORD),
    YIELD_TICKET(TargetType.SENDER, TargetType.NOTIFICATION, TargetType.ANNOUNCEMENT, TargetType.DISCORD),
    ASSIGN_TICKET(TargetType.SENDER, TargetType.NOTIFICATION, TargetType.ANNOUNCEMENT, TargetType.DISCORD),
    DONE_TICKET(TargetType.SENDER, TargetType.NOTIFICATION, TargetType.ANNOUNCEMENT, TargetType.DISCORD),
    REOPEN_TICKET(TargetType.SENDER, TargetType.NOTIFICATION, TargetType.ANNOUNCEMENT, TargetType.DISCORD),
    NOTE_TICKET(TargetType.SENDER, TargetType.NOTIFICATION, TargetType.ANNOUNCEMENT, TargetType.DISCORD),
    TELEPORT_TICKET(TargetType.SENDER);

    private final TargetType[] targets;

    MessageNames(TargetType... targets) {
        this.targets = targets;
    }

    public TargetType[] getTargets() {
        return targets;
    }
}