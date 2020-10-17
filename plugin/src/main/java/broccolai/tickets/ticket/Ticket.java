package broccolai.tickets.ticket;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class Ticket {

    private final int id;
    @NonNull
    private final UUID playerUUID;
    @NonNull
    private final List<Message> messages;
    @NonNull
    private final Location location;
    @NonNull
    private TicketStatus status;

    @Nullable
    private UUID pickerUUID;

    /**
     * Construct a new Ticket using all values
     *
     * @param id         Tickets id
     * @param playerUUID Players unique id
     * @param messages   List of messages
     * @param location   Tickets creation location
     * @param status     Tickets current status
     * @param pickerUUID Potentially unset pickers unique id
     */
    public Ticket(
            final int id,
            @NonNull final UUID playerUUID,
            @NonNull final List<Message> messages,
            @NonNull final Location location,
            @NonNull final TicketStatus status,
            @Nullable final UUID pickerUUID
    ) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.messages = messages;
        this.location = location;
        this.status = status;
        this.pickerUUID = pickerUUID;
    }

    /**
     * Get the most recent message reason by the player
     *
     * @return the message instance
     */
    @NonNull
    public Message currentMessage() {
        return messages
                .stream()
                .filter(message -> message.getReason() == MessageReason.MESSAGE)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Get the most recent note
     *
     * @return Last note
     */
    @NonNull
    public Optional<Message> lastNote() {
        return messages
                .stream()
                .filter(message -> message.getReason() == MessageReason.MESSAGE)
                .findFirst();
    }

    /**
     * Retrieve a LocalDateTime of when the ticket was first opened
     *
     * @return Local date time
     */
    @NonNull
    public LocalDateTime dateOpened() {
        return messages.get(0).getDate();
    }

    /**
     * Retrieve the tickets id
     *
     * @return Primitive integer
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieve the players unique id
     *
     * @return Unique id
     */
    @NonNull
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Retrieve the messages associated with the ticket
     *
     * @return List of messages
     */
    @NonNull
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Retrieve the tickets creation location
     *
     * @return Location instance
     */
    @NonNull
    public Location getLocation() {
        return location;
    }

    /**
     * Retrieve the tickets current status
     *
     * @return Tickets status
     */
    @NonNull
    public TicketStatus getStatus() {
        return status;
    }

    /**
     * Set the tickets current status to a new value
     *
     * @param status Status to assign
     */
    public void setStatus(@NonNull final TicketStatus status) {
        this.status = status;
    }

    /**
     * Retrieve the pickers unique id
     *
     * @return Potentially null unique id
     */
    @Nullable
    public UUID getPickerUUID() {
        return pickerUUID;
    }

    /**
     * Set the pickers unique id.
     *
     * @param pickerUUID Pickers unique id, or null if it's by the console
     */
    public void setPickerUUID(@Nullable final UUID pickerUUID) {
        this.pickerUUID = pickerUUID;
    }

    /**
     * Convert the ticket instance into a json object with all data.
     *
     * @return JSON object
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("name", Bukkit.getOfflinePlayer(playerUUID).getName());
        playerJson.addProperty("uuid", playerUUID.toString());
        json.add("player", playerJson);

        JsonObject locationJson = new JsonObject();
        World world = location.getWorld();
        locationJson.addProperty("world", world != null ? world.getName() : "unknown");
        locationJson.addProperty("x", location.getBlockX());
        locationJson.addProperty("y", location.getBlockY());
        locationJson.addProperty("z", location.getBlockZ());
        json.add("location", locationJson);

        json.addProperty("status", status.name());

        Optional<Message> lastNote = lastNote();
        Message currentMessage = currentMessage();

        json.addProperty("note", lastNote.map(Message::getData).orElse(null));
        json.addProperty("message", currentMessage.getData());

        return json;
    }

}
