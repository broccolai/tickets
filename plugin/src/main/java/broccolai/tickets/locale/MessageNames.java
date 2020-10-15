package broccolai.tickets.locale;

import org.jetbrains.annotations.NotNull;

/**
 * Enum representing potential message names
 */
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

    @NotNull
    private final TargetType[] targets;

    /**
     * Initialise a message name with target types.
     *
     * @param targets the corresponding target types
     */
    MessageNames(@NotNull final TargetType... targets) {
        this.targets = targets;
    }

    /**
     * Retrieve the targets.
     *
     * @return array of target types
     */
    @NotNull
    public TargetType[] getTargets() {
        return targets;
    }
}
