package broccolai.tickets.core.interactions;

import broccolai.tickets.core.locale.Message;

import java.util.Optional;

public enum NotificationReason {
    ASSIGN_TICKET(
            Message.SENDER__ASSIGN_TICKET,
            Message.NOTIFY__ASSIGN_TICKET,
            Message.ANNOUNCEMENT__ASSIGN_TICKET,
            true
    ),
    CLOSE_TICKET(
            Message.SENDER__CLOSE_TICKET,
            null,
            Message.ANNOUNCEMENT__CLOSE_TICKET,
            true
    ),
    DONE_TICKET(
            Message.SENDER__DONE_TICKET,
            Message.NOTIFY__DONE_TICKET,
            Message.ANNOUNCEMENT__DONE_TICKET,
            true
    ),
    NEW_TICKET(
            Message.SENDER__NEW_TICKET,
            null,
            Message.ANNOUNCEMENT__NEW_TICKET,
            true
    ),
    NOTE_TICKET(
            Message.SENDER__NOTE_TICKET,
            Message.NOTIFY__NOTE_TICKET,
            Message.ANNOUNCEMENT__NOTE_TICKET,
            true
    ),
    PICK_TICKET(
            Message.SENDER__PICK_TICKET,
            Message.NOTIFY__PICK_TICKET,
            Message.ANNOUNCEMENT__PICK_TICKET,
            true
    ),
    REOPEN_TICKET(
            Message.SENDER__REOPEN_TICKET,
            Message.NOTIFY__REOPEN_TICKET,
            Message.ANNOUNCEMENT__REOPEN_TICKET,
            true
    ),
    TELEPORT_TICKET(
            Message.SENDER__TELEPORT_TICKET,
            null,
            null,
            false
    ),
    UPDATE_TICKET(
            Message.SENDER__UPDATE_TICKET,
            null,
            Message.ANNOUNCEMENT__UPDATE_TICKET,
            true
    ),
    YIELD_TICKET(
            Message.SENDER__YIELD_TICKET,
            Message.NOTIFY__YIELD_TICKET,
            Message.ANNOUNCEMENT__YIELD_TICKET,
            true
    );

    private final Message sender;
    private final Message notify;
    private final Message announcement;
    private final boolean discord;

    NotificationReason(
            final Message sender,
            final Message notify,
            final Message announcement,
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
    public Optional<Message> sender() {
        return Optional.ofNullable(sender);
    }

    /**
     * @return Notifies
     */
    public Optional<Message> notifies() {
        return Optional.ofNullable(notify);
    }

    /**
     * @return Announcement
     */
    public Optional<Message> announcement() {
        return Optional.ofNullable(announcement);
    }

    /**
     * @return Discord
     */
    public boolean discord() {
        return discord;
    }
}
