package broccolai.tickets.message;

import broccolai.tickets.utilities.Dirtyable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public final class Message implements Dirtyable {

    private boolean dirty = false;

    @NonNull
    private final MessageReason reason;
    @NonNull
    private final LocalDateTime date;
    @Nullable
    private final String data;
    @Nullable
    private final UUID sender;

    private Message(
            final @NonNull MessageReason reason,
            final @NonNull LocalDateTime date,
            final @Nullable String data,
            final @Nullable UUID sender
    ) {
        this.reason = reason;
        this.date = date;
        this.data = data;
        this.sender = sender;
    }

    /**
     * Create a message with just data
     *
     * @param reason Reason for creating the message
     * @param date   Date of the message
     * @param data   String data
     * @return Constructed Ticket
     */
    public static @NonNull Message create(
            final @NonNull MessageReason reason,
            final @NonNull LocalDateTime date,
            final @Nullable String data
    ) {
        Message message = new Message(reason, date, data, null);
        message.dirty = true;

        return message;
    }

    /**
     * Create a message with just sender
     *
     * @param reason Reason for creating the message
     * @param date   Date of the message
     * @param sender Unique id of the message creator
     * @return Constructed Ticket
     */
    public static @NonNull Message create(
            final @NonNull MessageReason reason,
            final @NonNull LocalDateTime date,
            final @Nullable UUID sender
    ) {
        Message message = new Message(reason, date, null, sender);
        message.dirty = true;

        return message;
    }

    /**
     * Create a message with data and sender
     *
     * @param reason Reason for creating the message
     * @param date   Date of the message
     * @param data   String data
     * @param sender Unique id of the message creator
     * @return Constructed Ticket
     */
    public static @NonNull Message create(
            final @NonNull MessageReason reason,
            final @NonNull LocalDateTime date,
            final @Nullable String data,
            final @Nullable UUID sender
    ) {
        Message message = new Message(reason, date, data, sender);
        message.dirty = true;

        return message;
    }

    /**
     * Load a message with data and sender
     *
     * @param reason Reason for creating the message
     * @param date   Date of the message
     * @param data   String data
     * @param sender Unique id of the message creator
     * @return Constructed Ticket
     */
    public static @NonNull Message load(
            final @NonNull MessageReason reason,
            final @NonNull LocalDateTime date,
            final @Nullable String data,
            final @Nullable UUID sender
    ) {
        return new Message(reason, date, data, sender);
    }

    /**
     * Get the message creation reason.
     *
     * @return the MessageReason
     */
    public @NonNull MessageReason getReason() {
        return reason;
    }

    /**
     * Get the message creation date.
     *
     * @return the LocalDateTime
     */
    public @NonNull LocalDateTime getDate() {
        return date;
    }

    /**
     * Get the message data.
     *
     * @return the data
     */
    public @Nullable String getData() {
        return data;
    }

    /**
     * Get the message creator.
     *
     * @return the unique id
     */
    public @Nullable UUID getSender() {
        return sender;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

}
