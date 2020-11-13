package broccolai.tickets.core.interactions;

import broccolai.tickets.core.locale.NewMessages;

import java.util.Optional;

public enum NotificationReason {
    ASSIGN_TICKET(
            NewMessages.SENDER__ASSIGN_TICKET,
            NewMessages.NOTIFY__ASSIGN_TICKET,
            NewMessages.ANNOUNCEMENT__ASSIGN_TICKET,
            true
    ),
    CLOSE_TICKET(
            NewMessages.SENDER__CLOSE_TICKET,
            null,
            NewMessages.ANNOUNCEMENT__CLOSE_TICKET,
            true
    ),
    DONE_TICKET(
            NewMessages.SENDER__DONE_TICKET,
            NewMessages.NOTIFY__DONE_TICKET,
            NewMessages.ANNOUNCEMENT__DONE_TICKET,
            true
    ),
    NEW_TICKET(
            NewMessages.SENDER__NEW_TICKET,
            null,
            NewMessages.ANNOUNCEMENT__NEW_TICKET,
            true
    ),
    NOTE_TICKET(
            NewMessages.SENDER__NOTE_TICKET,
            NewMessages.NOTIFY__NOTE_TICKET,
            NewMessages.ANNOUNCEMENT__NOTE_TICKET,
            true
    ),
    PICK_TICKET(
            NewMessages.SENDER__PICK_TICKET,
            NewMessages.NOTIFY__PICK_TICKET,
            NewMessages.ANNOUNCEMENT__PICK_TICKET,
            true
    ),
    REOPEN_TICKET(
            NewMessages.SENDER__REOPEN_TICKET,
            NewMessages.NOTIFY__REOPEN_TICKET,
            NewMessages.ANNOUNCEMENT__REOPEN_TICKET,
            true
    ),
    TELEPORT_TICKET(
            NewMessages.SENDER__TELEPORT_TICKET,
            null,
            null,
            false
    ),
    UPDATE_TICKET(
            NewMessages.SENDER__UPDATE_TICKET,
            null,
            NewMessages.ANNOUNCEMENT__UPDATE_TICKET,
            true
    ),
    YIELD_TICKET(
            NewMessages.SENDER__YIELD_TICKET,
            NewMessages.NOTIFY__YIELD_TICKET,
            NewMessages.ANNOUNCEMENT__YIELD_TICKET,
            true
    );

    private final NewMessages sender;
    private final NewMessages notify;
    private final NewMessages announcement;
    private final boolean discord;

    NotificationReason(
            final NewMessages sender,
            final NewMessages notify,
            final NewMessages announcement,
            final boolean discord
    ) {
        this.sender = sender;
        this.notify = notify;
        this.announcement = announcement;
        this.discord = discord;
    }

    /**
     * @return Sender
     */
    public Optional<NewMessages> sender() {
        return Optional.ofNullable(sender);
    }

    /**
     * @return Notifies
     */
    public Optional<NewMessages> notifies() {
        return Optional.ofNullable(notify);
    }

    /**
     * @return Announcement
     */
    public Optional<NewMessages> announcement() {
        return Optional.ofNullable(announcement);
    }

    /**
     * @return Discord
     */
    public boolean discord() {
        return discord;
    }
}
