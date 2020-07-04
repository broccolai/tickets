package co.uk.magmo.puretickets.locale;

import static co.uk.magmo.puretickets.locale.TargetType.*;

public enum MessageNames {
    NEW_TICKET(SENDER, ANNOUNCEMENT, DISCORD),
    UPDATE_TICKET(SENDER, ANNOUNCEMENT, DISCORD),
    CLOSE_TICKET(SENDER, ANNOUNCEMENT, DISCORD),
    PICK_TICKET(SENDER, NOTIFICATION, ANNOUNCEMENT, DISCORD),
    YIELD_TICKET(SENDER, NOTIFICATION, ANNOUNCEMENT, DISCORD),
    ASSIGN_TICKET(SENDER, NOTIFICATION, ANNOUNCEMENT, DISCORD),
    DONE_TICKET(SENDER, NOTIFICATION, ANNOUNCEMENT, DISCORD),
    REOPEN_TICKET(SENDER, NOTIFICATION, ANNOUNCEMENT, DISCORD),
    NOTE_TICKET(SENDER, NOTIFICATION, ANNOUNCEMENT, DISCORD),
    TELEPORT_TICKET(SENDER);

    private final TargetType[] targets;

    MessageNames(TargetType... targets) {
        this.targets = targets;
    }

    public TargetType[] getTargets() {
        return targets;
    }
}