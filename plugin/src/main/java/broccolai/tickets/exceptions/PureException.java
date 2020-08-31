package broccolai.tickets.exceptions;

import broccolai.tickets.locale.Messages;
import org.jetbrains.annotations.Nullable;

public class PureException extends Exception {
    @Nullable
    private final Messages message;
    @Nullable
    private final String[] replacements;
    @Nullable
    private final String value;

    public PureException() {
        this.message = null;
        this.replacements = null;
        this.value = null;
    }

    public PureException(String value) {
        this.message = null;
        this.replacements = null;
        this.value = value;
    }

    public PureException(Messages message, String... replacements) {
        this.message = message;
        this.replacements = replacements;
        this.value = null;
    }

    public Messages getMessageKey() {
        return message;
    }

    public String[] getReplacements() {
        return replacements;
    }

    public String getValue() {
        return value;
    }
}
