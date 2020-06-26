package co.uk.magmo.puretickets.ticket;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message {
    MessageReason reason;
    LocalDateTime date;

    @Nullable
    String data;
    @Nullable
    UUID sender;

    public Message(MessageReason reason, LocalDateTime date, @Nullable String data) {
        this.reason = reason;
        this.date = date;
        this.data = data;
    }

    public Message(MessageReason reason, LocalDateTime date, @Nullable UUID sender) {
        this.reason = reason;
        this.date = date;
        this.sender = sender;
    }

    public MessageReason getReason() {
        return reason;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public @Nullable String getData() {
        return data;
    }

    public @Nullable UUID getSender() {
        return sender;
    }
}