package broccolai.tickets.ticket;

import broccolai.corn.core.Lists;
import com.google.common.collect.Iterables;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Ticket {
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
     * Construct a new Ticket using all values.
     * @param id the tickets id
     * @param playerUUID the players unique id
     * @param messages the list of messages
     * @param location the tickets creation location
     * @param status the tickets current status
     * @param pickerUUID the potentially unset pickers unique id
     */
    public Ticket(int id, @NotNull UUID playerUUID, @NotNull List<Message> messages, @NotNull Location location, @NotNull TicketStatus status, @Nullable UUID pickerUUID) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.messages = messages;
        this.location = location;
        this.status = status;
        this.pickerUUID = pickerUUID;
    }

    /**
     * Get the most recent message reason by the player.
     * @return the message instance
     */
    @NotNull
    public Message currentMessage() {
        return Iterables.getLast(Lists.filter(messages, message -> message.getReason() == MessageReason.MESSAGE));
    }

    /**
     * Retrieve a LocalDateTime of when the ticket was first opened.
     * @return a local date time
     */
    @NotNull
    public LocalDateTime dateOpened() {
        return messages.get(0).getDate();
    }

    /**
     * Retrieve the tickets id.
     * @return a primitive integer
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieve the players unique id.
     * @return the unique id
     */
    @NotNull
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Retrieve the messages associated with the ticket.
     * @return a list of messages
     */
    @NotNull
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Retrieve the tickets creation location.
     * @return a location instance
     */
    @NotNull
    public Location getLocation() {
        return location;
    }

    /**
     * Retrieve the tickets current status.
     * @return the tickets status
     */
    @NotNull
    public TicketStatus getStatus() {
        return status;
    }

    /**
     * Set the tickets current status to a new value.
     * @param status the status to assign
     */
    public void setStatus(@NotNull TicketStatus status) {
        this.status = status;
    }

    /**
     * Retrieve the pickers unique id.
     * @return a potentially null unique id
     */
    @Nullable
    public UUID getPickerUUID() {
        return pickerUUID;
    }

    /**
     * Set the pickers unique id.
     * @param pickerUUID the pickers unique id, or null if it's by the console
     */
    public void setPickerUUID(@Nullable UUID pickerUUID) {
        this.pickerUUID = pickerUUID;
    }

    /**
     * Convert the ticket instance into a json object with all data.
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
        json.addProperty("message", currentMessage().getData());

        return json;
    }
}