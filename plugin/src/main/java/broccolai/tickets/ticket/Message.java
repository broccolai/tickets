package broccolai.tickets.ticket;

import java.time.LocalDateTime;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class representing a ticket message.
 */
public class Message {
    @NotNull
    private final MessageReason reason;
    @NotNull
    private final LocalDateTime date;

    @Nullable
    private String data;
    @Nullable
    private UUID sender;

    /**
     * Create a message with just data.
     *
     * @param reason the reason for creating the message
     * @param date   the date of the message
     * @param data   the string data
     */
    public Message(@NotNull MessageReason reason, @NotNull LocalDateTime date, @Nullable String data) {
        this.reason = reason;
        this.date = date;
        this.data = data;
    }

    /**
     * Create a message with just sender.
     *
     * @param reason the reason for creating the message
     * @param date   the date of the message
     * @param sender the unique id of the message creator
     */
    public Message(@NotNull MessageReason reason, @NotNull LocalDateTime date, @Nullable UUID sender) {
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
    public Message(@NotNull MessageReason reason, @NotNull LocalDateTime date, @Nullable String data, @Nullable UUID sender) {
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
    @NotNull
    public MessageReason getReason() {
        return reason;
    }

    /**
     * Get the message creation date.
     *
     * @return the LocalDateTime
     */
    @NotNull
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