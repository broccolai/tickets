package broccolai.tickets.ticket;

import broccolai.corn.core.Lists;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Ticket {

    private final int id;
    @NotNull
    private final UUID playerUUID;
    @NotNull
    private final List<Message> messages;
    @NotNull
    private final Location location;
    @NotNull
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
            @NotNull final UUID playerUUID,
            @NotNull final List<Message> messages,
            @NotNull final Location location,
            @NotNull final TicketStatus status,
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
     * Get the most recent message reason by the player.
     *
     * @return the message instance
     */
    @NotNull
    public Message currentMessage() {
        return Objects.requireNonNull(Lists.last(messages, m -> m.getReason() == MessageReason.MESSAGE));
    }

    /**
     * Get the most recent note, or null
     *
     * @return Last note
     */
    @Nullable
    public Message lastNote() {
        return Lists.last(messages, m -> m.getReason() == MessageReason.NOTE);
    }

    /**
     * Retrieve a LocalDateTime of when the ticket was first opened.
     *
     * @return a local date time
     */
    @NotNull
    public LocalDateTime dateOpened() {
        return messages.get(0).getDate();
    }

    /**
     * Retrieve the tickets id.
     *
     * @return a primitive integer
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieve the players unique id.
     *
     * @return the unique id
     */
    @NotNull
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Retrieve the messages associated with the ticket.
     *
     * @return a list of messages
     */
    @NotNull
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Retrieve the tickets creation location.
     *
     * @return a location instance
     */
    @NotNull
    public Location getLocation() {
        return location;
    }

    /**
     * Retrieve the tickets current status.
     *
     * @return the tickets status
     */
    @NotNull
    public TicketStatus getStatus() {
        return status;
    }

    /**
     * Set the tickets current status to a new value.
     *
     * @param status the status to assign
     */
    public void setStatus(@NotNull final TicketStatus status) {
        this.status = status;
    }

    /**
     * Retrieve the pickers unique id.
     *
     * @return a potentially null unique id
     */
    @Nullable
    public UUID getPickerUUID() {
        return pickerUUID;
    }

    /**
     * Set the pickers unique id.
     *
     * @param pickerUUID the pickers unique id, or null if it's by the console
     */
    public void setPickerUUID(@Nullable final UUID pickerUUID) {
        this.pickerUUID = pickerUUID;
    }

    /**
     * Convert the ticket instance into a json object with all data.
     *
     * @return a json object
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

        String data = null;
        Message lastNote = lastNote();

        if (lastNote != null) {
            data = lastNote.getData();
        }

        json.addProperty("note", data);
        json.addProperty("message", currentMessage().getData());

        return json;
    }

}
