package broccolai.tickets.exceptions;

import broccolai.tickets.locale.Messages;

public class PureException extends Exception {
    private final Messages message;
    private final String[] replacements;

    public PureException(Messages message, String... replacements) {
        this.message = message;
        this.replacements = replacements;
    }

    public Messages getMessageKey() {
        return message;
    }

    public String[] getReplacements() {
        return replacements;
    }
}
