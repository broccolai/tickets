package broccolai.tickets.ticket;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Class representing a ticket message
 */
public class Message {

    @NonNull
    private final MessageReason reason;
    @NonNull
    private final LocalDateTime date;

    @Nullable
    private String data;
    @Nullable
    private UUID sender;

    /**
     * Create a message with just data
     *
     * @param reason Reason for creating the message
     * @param date   Date of the message
     * @param data   String data
     */
    public Message(
            @NonNull final MessageReason reason,
            @NonNull final LocalDateTime date,
            @Nullable final String data
    ) {
        this.reason = reason;
        this.date = date;
        this.data = data;
    }

    /**
     * Create a message with just sender
     *
     * @param reason Reason for creating the message
     * @param date   Date of the message
     * @param sender Unique id of the message creator
     */
    public Message(@NonNull final MessageReason reason, @NonNull final LocalDateTime date, @Nullable final UUID sender) {
        this.reason = reason;
        this.date = date;
        this.sender = sender;
    }

    /**
     * Create a message with data and sender.
     *
     * @param reason the reason for creating the message
     * @param date   the date of the message
     * @param data   the string data
     * @param sender the unique id of the message creator
     */
    public Message(
            @NonNull final MessageReason reason,
            @NonNull final LocalDateTime date,
            @Nullable final String data,
            @Nullable final UUID sender
    ) {
        this.reason = reason;
        this.date = date;
        this.data = data;
        this.sender = sender;
    }

    /**
     * Get the message creation reason.
     *
     * @return the MessageReason
     */
    @NonNull
    public MessageReason getReason() {
        return reason;
    }

    /**
     * Get the message creation date.
     *
     * @return the LocalDateTime
     */
    @NonNull
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Get the message data.
     *
     * @return the data
     */
    @Nullable
    public String getData() {
        return data;
    }

    /**
     * Get the message creator.
     *
     * @return the unique id
     */
    @Nullable
    public UUID getSender() {
        return sender;
    }

}
