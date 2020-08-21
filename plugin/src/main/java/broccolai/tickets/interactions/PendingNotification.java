package broccolai.tickets.interactions;

import broccolai.tickets.locale.Messages;

public class PendingNotification {
    private final Messages messageKey;
    private final String[] replacements;

    public PendingNotification(Messages messageKey, String[] replacements) {
        this.messageKey = messageKey;
        this.replacements = replacements;
    }

    public Messages getMessageKey() {
        return messageKey;
    }

    public String[] getReplacements() {
        return replacements;
    }
}