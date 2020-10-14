package broccolai.tickets.exceptions;

import broccolai.corn.spigot.locale.LocaleKeys;
import broccolai.tickets.locale.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an base exception class for other PureExceptions to inherit.
 */
public class PureException extends RuntimeException {
    @Nullable
    private final LocaleKeys message;
    @Nullable
    private final String[] replacements;
    @Nullable
    private final String value;

    /**
     * Initialise a blank PureException.
     */
    public PureException() {
        this.message = null;
        this.replacements = null;
        this.value = null;
    }

    /**
     * Initialise a plain PureException.
     *
     * @param value the message to give
     */
    public PureException(@Nullable String value) {
        this.message = null;
        this.replacements = null;
        this.value = value;
    }

    /**
     * Initialise a localised PureException.
     *
     * @param message      the locale message key
     * @param replacements the replacements to use
     */
    public PureException(@Nullable Messages message, @NotNull String... replacements) {
        this.message = message;
        this.replacements = replacements;
        this.value = null;
    }

    /**
     * Get the locale message key.
     *
     * @return the Messages entry
     */
    @Nullable
    public LocaleKeys getMessageKey() {
        return message;
    }

    /**
     * Get the locale replacements.
     *
     * @return an array of replacements
     */
    @Nullable
    public String[] getReplacements() {
        return replacements;
    }

    /**
     * Get the plain message.
     *
     * @return the message
     */
    @Nullable
    public String getValue() {
        return value;
    }
}
