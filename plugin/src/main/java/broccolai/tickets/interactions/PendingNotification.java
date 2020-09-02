package broccolai.tickets.interactions;

import broccolai.tickets.locale.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Class representing a localised notification that has not been sent.
 */
public class PendingNotification {
    @NotNull
    private final Messages messageKey;
    @NotNull
    private final String[] replacements;

    /**
     * Initialise a pending notification.
     *
     * @param messageKey   the localised message key
     * @param replacements the replacements to use
     */
    public PendingNotification(@NotNull Messages messageKey, @NotNull String[] replacements) {
        this.messageKey = messageKey;
        this.replacements = replacements;
    }

    @NotNull
    public Messages getMessageKey() {
        return messageKey;
    }

    @NotNull
    public String[] getReplacements() {
        return replacements;
    }
}