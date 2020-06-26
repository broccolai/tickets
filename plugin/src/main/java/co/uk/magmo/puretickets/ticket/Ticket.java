package co.uk.magmo.puretickets.ticket;

import com.google.common.collect.Iterables;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Ticket {
    Integer id;
    UUID playerUUID;
    ArrayList<Message> messages;
    TicketStatus status;
    Location location;

    @Nullable
    UUID pickerUUID;

    public Ticket(Integer id, UUID playerUUID, ArrayList<Message> messages, TicketStatus status, Location location, @Nullable UUID pickerUUID) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.messages = messages;
        this.status = status;
        this.location = location;
        this.pickerUUID = pickerUUID;
    }

    public Message currentMessage() {
        return Iterables.getLast(messages);
    }

    public LocalDateTime dateOpened() {
        return messages.get(0).getDate();
    }
}