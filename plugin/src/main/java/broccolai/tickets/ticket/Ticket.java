package broccolai.tickets.ticket;

import broccolai.tickets.exceptions.TicketClosed;
import broccolai.tickets.exceptions.TicketOpen;
import broccolai.tickets.message.Message;
import broccolai.tickets.message.MessageReason;
import broccolai.tickets.utilities.Dirtyable;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class Ticket implements Dirtyable {

    private boolean dirty = false;

    private final int id;
    private final UUID playerUUID;
    private final Location location;
    private TicketStatus status;
    private UUID pickerUUID;

    private final List<Message> messages = new ArrayList<>();

    /**
     * Construct a new Ticket using all values
     *
     * @param id         Tickets id
     * @param playerUUID Players unique id
     * @param location   Tickets creation location
     * @param status     Tickets current status
     * @param pickerUUID Potentially unset pickers unique id
     */
    public Ticket(
            final int id,
            @NonNull final UUID playerUUID,
            @NonNull final Location location,
            @NonNull final TicketStatus status,
            @Nullable final UUID pickerUUID
    ) {
        this.id = id;
        this.playerUUID = playerUUID;
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
                .filter(message -> message.getReason() == MessageReason.NOTE)
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
     * Retrieve the pickers unique id
     *
     * @return Potentially null unique id
     */
    @Nullable
    public UUID getPickerUUID() {
        return pickerUUID;
    }

    /**
     * Adds a new message to a ticket instance
     *
     * @param message the message to add to the ticket
     * @throws TicketClosed if the ticket is closed
     */
    public void update(@NonNull final Message message) throws TicketClosed {
        if (status == TicketStatus.CLOSED) {
            throw new TicketClosed();
        }

        dirty = true;
        messages.add(message);
    }

    /**
     * Picks the supplied actioners unique id
     *
     * @param uuid the actioners unique id
     * @throws TicketClosed if the ticket is closed
     */
    public void pick(@Nullable final UUID uuid) throws TicketClosed {
        if (status == TicketStatus.CLOSED) {
            throw new TicketClosed();
        }

        dirty = true;
        status = TicketStatus.PICKED;
        pickerUUID = uuid;
        messages.add(Message.create(MessageReason.PICKED, LocalDateTime.now(), uuid));
    }

    /**
     * Yields a ticket using the supplied actioners unique id
     *
     * @param uuid the actioners unique id
     * @throws TicketOpen if the ticket is open
     */
    public void yield(@Nullable final UUID uuid) throws TicketOpen {
        if (status == TicketStatus.OPEN) {
            throw new TicketOpen();
        }

        dirty = true;
        status = TicketStatus.OPEN;
        pickerUUID = uuid;
        messages.add(Message.create(MessageReason.YIELDED, LocalDateTime.now(), uuid));
    }

    /**
     * Closes a ticket using the supplied actioners unique id
     *
     * @param uuid the actioners unique id
     * @throws TicketClosed if the ticket is already closed
     */
    public void close(@Nullable final UUID uuid) throws TicketClosed {
        if (status == TicketStatus.CLOSED) {
            throw new TicketClosed();
        }

        dirty = true;
        status = TicketStatus.CLOSED;
        messages.add(Message.create(MessageReason.CLOSED, LocalDateTime.now(), uuid));
    }

    /**
     * Done-marks a ticket using the supplied actioners unique id
     *
     * @param uuid the actioners unique id
     * @throws TicketClosed if the ticket is already closed
     */
    public void done(@Nullable final UUID uuid) throws TicketClosed {
        if (status == TicketStatus.CLOSED) {
            throw new TicketClosed();
        }

        dirty = true;
        status = TicketStatus.CLOSED;
        messages.add(Message.create(MessageReason.DONE_MARKED, LocalDateTime.now(), uuid));
    }

    /**
     * Opens a ticket using the supplied actioners unique id
     *
     * @param uuid the actioners unique id
     * @throws TicketOpen if the ticket is already open
     */
    public void reopen(@Nullable final UUID uuid) throws TicketOpen {
        if (status == TicketStatus.OPEN) {
            throw new TicketOpen();
        }

        dirty = true;
        status = TicketStatus.OPEN;
        messages.add(Message.create(MessageReason.REOPENED, LocalDateTime.now(), uuid));
    }

    /**
     * Adds a note to a ticket using the supplied actioners unique id
     *
     * @param uuid  the actioners unique id
     * @param input the note message
     */
    public void note(@Nullable final UUID uuid, @NonNull final String input) {
        dirty = true;
        messages.add(Message.create(MessageReason.NOTE, LocalDateTime.now(), input, uuid));
    }

    /**
     * Add a message to the tickets storage
     *
     * @param message Message to add
     */
    public void withMessage(final Message message) {
        messages.add(message);
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

    @Override
    public boolean isDirty() {
        return dirty;
    }

}
