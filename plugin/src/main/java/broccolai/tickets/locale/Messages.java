package broccolai.tickets.locale;

import broccolai.corn.spigot.locale.LocaleKeys;
import org.jetbrains.annotations.NotNull;

/**
 * Enum representing locale messages.
 */
@SuppressWarnings("unused")
public enum Messages implements LocaleKeys {

    // GENERAL
    GENERAL__PREFIX(false),
    GENERAL__LIST_FORMAT(false),
    GENERAL__LIST_HEADER_FORMAT(false),
    GENERAL__LOG_FORMAT(false),
    GENERAL__HS_FORMAT(false),
    // SENDER
    SENDER__NEW_TICKET(true),
    SENDER__UPDATE_TICKET(true),
    SENDER__CLOSE_TICKET(true),
    SENDER__PICK_TICKET(true),
    SENDER__YIELD_TICKET(true),
    SENDER__ASSIGN_TICKET(true),
    SENDER__DONE_TICKET(true),
    SENDER__REOPEN_TICKET(true),
    SENDER__NOTE_TICKET(true),
    SENDER__TELEPORT_TICKET(true),
    // NOTIFICATION
    NOTIFICATION__PICK_TICKET(true),
    NOTIFICATION__YIELD_TICKET(true),
    NOTIFICATION__ASSIGN_TICKET(true),
    NOTIFICATION__DONE_TICKET(true),
    NOTIFICATION__REOPEN_TICKET(true),
    NOTIFICATION__NOTE_TICKET(true),
    // ANNOUNCEMENT
    ANNOUNCEMENT__NEW_TICKET(true),
    ANNOUNCEMENT__UPDATE_TICKET(true),
    ANNOUNCEMENT__CLOSE_TICKET(true),
    ANNOUNCEMENT__PICK_TICKET(true),
    ANNOUNCEMENT__YIELD_TICKET(true),
    ANNOUNCEMENT__ASSIGN_TICKET(true),
    ANNOUNCEMENT__DONE_TICKET(true),
    ANNOUNCEMENT__REOPEN_TICKET(true),
    ANNOUNCEMENT__NOTE_TICKET(true),
    // TITLES
    TITLES__SPECIFIC_TICKETS(false),
    TITLES__YOUR_TICKETS(false),
    TITLES__ALL_TICKETS(false),
    TITLES__SPECIFIC_STATUS(false),
    TITLES__TICKET_STATUS(false),
    TITLES__SHOW_TICKET(false),
    TITLES__TICKET_LOG(false),
    TITLES__HIGHSCORES(false),
    // SHOW NAMES
    SHOW__SENDER(false),
    SHOW__PICKER(false),
    SHOW__UNPICKED(false),
    SHOW__MESSAGE(false),
    SHOW__LOCATION(false),
    // EXCEPTIONS
    EXCEPTIONS__TICKET_NOT_FOUND(true),
    EXCEPTIONS__INVALID_SETTING_TYPE(true),
    EXCEPTIONS__TOO_MANY_OPEN_TICKETS(true),
    EXCEPTIONS__TICKET_CLOSED(true),
    EXCEPTIONS__TICKET_OPEN(true),
    // OTHER
    OTHER__REMINDER(false),
    OTHER__SETTING_UPDATE(false);

    private final boolean hasPrefix;

    Messages(final boolean hasPrefix) {
        this.hasPrefix = hasPrefix;
    }

    @Override
    public @NotNull String getName() {
        return name().toLowerCase().replace("__", ".");
    }

    @Override
    public boolean hasPrefix() {
        return hasPrefix;
    }


    /**
     * Retrieve a Message using a target type and a message name.
     *
     * @param targetType   the target type
     * @param messageNames the message name
     * @return a message
     */
    @NotNull
    public static Messages retrieve(@NotNull final TargetType targetType, @NotNull final MessageNames messageNames) {
        return valueOf(targetType.name() + "__" + messageNames.name());
    }
}
