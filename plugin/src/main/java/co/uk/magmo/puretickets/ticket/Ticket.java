package co.uk.magmo.puretickets.ticket;

import co.uk.magmo.puretickets.utilities.generic.ListUtilities;
import com.google.common.collect.Iterables;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Ticket {
    private final Integer id;
    private final UUID playerUUID;
    private final List<Message> messages;
    private final Location location;
    private TicketStatus status;

    @Nullable
    private UUID pickerUUID;

    public Ticket(Integer id, UUID playerUUID, List<Message> messages, Location location, TicketStatus status, @Nullable UUID pickerUUID) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.messages = messages;
        this.location = location;
        this.status = status;
        this.pickerUUID = pickerUUID;
    }

    public Message currentMessage() {
        return Iterables.getLast(ListUtilities.filter(messages, message -> message.getReason() == MessageReason.MESSAGE));
    }

    public LocalDateTime dateOpened() {
        return messages.get(0).getDate();
    }

    public Integer getId() {
        return id;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Location getLocation() {
        return location;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public @Nullable UUID getPickerUUID() {
        return pickerUUID;
    }

    public void setPickerUUID(@Nullable UUID pickerUUID) {
        this.pickerUUID = pickerUUID;
    }
}