package broccolai.tickets.ticket;

import java.time.LocalDateTime;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class Message {
    private final MessageReason reason;
    private final LocalDateTime date;

    @Nullable
    private String data;
    @Nullable
    private UUID sender;

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

    public Message(MessageReason reason, LocalDateTime date, @Nullable String data, @Nullable UUID sender) {
        this.reason = reason;
        this.date = date;
        this.data = data;
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