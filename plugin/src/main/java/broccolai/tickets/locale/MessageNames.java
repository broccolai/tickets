package broccolai.tickets.locale;

import org.checkerframework.checker.nullness.qual.NonNull;

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

    @NonNull
    private final TargetType[] targets;

    /**
     * Initialise a message name with target types.
     *
     * @param targets the corresponding target types
     */
    MessageNames(final @NonNull TargetType... targets) {
        this.targets = targets;
    }

    /**
     * Retrieve the targets.
     *
     * @return array of target types
     */
    public @NonNull TargetType[] getTargets() {
        return targets;
    }
}
