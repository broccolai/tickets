package broccolai.tickets.ticket;

import broccolai.corn.core.Lists;
import com.google.common.collect.Iterables;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

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
        return Iterables.getLast(Lists.filter(messages, message -> message.getReason() == MessageReason.MESSAGE));
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

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("name", Bukkit.getOfflinePlayer(playerUUID).getName());
        playerJson.addProperty("uuid", playerUUID.toString());
        json.add("player", playerJson);

        JsonObject locationJson = new JsonObject();
        locationJson.addProperty("world", location.getWorld().getName());
        locationJson.addProperty("x", location.getBlockX());
        locationJson.addProperty("y", location.getBlockY());
        locationJson.addProperty("z", location.getBlockZ());
        json.add("location", locationJson);

        json.addProperty("status", status.name());
        json.addProperty("message", currentMessage().getData());

        return json;
    }
}