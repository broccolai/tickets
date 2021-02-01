package broccolai.tickets.core.ticket;

import broccolai.tickets.core.exceptions.TicketClosed;
import broccolai.tickets.core.exceptions.TicketOpen;
import broccolai.tickets.core.message.Message;
import broccolai.tickets.core.message.MessageReason;
import broccolai.tickets.core.user.User;
import broccolai.tickets.core.user.UserManager;
import broccolai.tickets.core.utilities.Dirtyable;
import broccolai.tickets.core.utilities.TicketLocation;
import broccolai.tickets.core.utilities.TimeUtilities;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class Ticket implements Dirtyable {

    private final UserManager<?, ?, ?> userManager;

    private boolean dirty = false;

    private final int id;
    private final UUID playerUUID;
    private final TicketLocation location;
    private @NonNull TicketStatus status;
    private @Nullable UUID pickerUUID;

    private final List<Message> messages = new ArrayList<>();

    /**
     * Construct a new Ticket using all values
     *
     * @param userManager User manager
     * @param id          Tickets id
     * @param playerUUID  Players unique id
     * @param location    Tickets creation location
     * @param status      Tickets current status
     * @param pickerUUID  Potentially unset pickers unique id
     */
    public Ticket(
            final @NonNull UserManager<?, ?, ?> userManager,
            final int id,
            final @NonNull UUID playerUUID,
            final @NonNull TicketLocation location,
            final @NonNull TicketStatus status,
            final @Nullable UUID pickerUUID
    ) {
        this.userManager = userManager;
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
    public @NonNull Message currentMessage() {
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
    public @NonNull Optional<Message> lastNote() {
        return messages
                .stream()
                .filter(message -> message.getReason() == MessageReason.NOTE)
                .findFirst();
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
    public @NonNull User getPlayer() {
        return this.userManager.getUser(playerUUID);
    }

    /**
     * Retrieve the pickers unique id
     *
     * @return Potentially null unique id
     */
    public @NonNull Optional<User> getPicker() {
        if (pickerUUID == null) {
            return Optional.empty();
        }

        return Optional.of(this.userManager.getUser(pickerUUID));
    }

    /**
     * Retrieve the messages associated with the ticket
     *
     * @return List of messages
     */
    public @NonNull List<Message> getMessages() {
        return messages;
    }

    /**
     * Retrieve the tickets creation location
     *
     * @return Location instance
     */
    public @NonNull TicketLocation getLocation() {
        return location;
    }

    /**
     * Retrieve the tickets current status
     *
     * @return Tickets status
     */
    public @NonNull TicketStatus getStatus() {
        return status;
    }

    /**
     * Retrieve unique id of creator
     *
     * @return Unique id of creator
     */
    public @NonNull UUID getPlayerUniqueID() {
        return this.playerUUID;
    }

    /**
     * Retrieve the pickers unique id
     *
     * @return Potentially null unique id
     */
    public @NotNull Optional<UUID> getPickerUniqueId() {
        return Optional.ofNullable(pickerUUID);
    }

    /**
     * Adds a new message to a ticket instance
     *
     * @param message the message to add to the ticket
     * @throws TicketClosed if the ticket is closed
     */
    public void update(final @NonNull Message message) throws TicketClosed {
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
    public void pick(final @Nullable UUID uuid) throws TicketClosed {
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
    public void yield(final @Nullable UUID uuid) throws TicketOpen {
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
    public void close(final @Nullable UUID uuid) throws TicketClosed {
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
    public void done(final @Nullable UUID uuid) throws TicketClosed {
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
    public void reopen(final @Nullable UUID uuid) throws TicketOpen {
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
    public void note(final @Nullable UUID uuid, final @NonNull String input) {
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
     * @return Get templates
     */
    public @NonNull Template[] templates() {
        Template[] results = new Template[12];

        results[0] = Template.of("id", String.valueOf(this.id));

        Message message = this.currentMessage();

        String messageData = message.getData();
        results[1] = Template.of("message", messageData);

        String messageDate = TimeUtilities.formatted(message.getDate());
        results[2] = Template.of("date", messageDate);

        String status = this.status.name();
        results[3] = Template.of("status", status);

        TextColor color = this.status.getColor();
        results[4] = Template.of("color", color.asHexString());

        String player = this.getPlayer().getName();
        results[5] = Template.of("player", player);

        String picker = this.getPicker().map(User::getName).orElse("unpicked");
        results[6] = Template.of("picker", picker);

        String world = this.location.getWorld();
        results[7] = Template.of("world", world);

        String x = String.valueOf(this.location.getX());
        results[8] = Template.of("x", x);

        String y = String.valueOf(this.location.getY());
        results[9] = Template.of("y", y);

        String z = String.valueOf(this.location.getZ());
        results[10] = Template.of("z", z);

        TextComponent.Builder identifier = Component.text()
                .content("#")
                .color(this.status.getColor())
                .append(Component.text(this.id));

        final HoverEvent<Component> hover = HoverEvent.showText(
                Component.text()
                        .append(
                                Component.text()
                                        .content("Ticket ")
                                        .append(identifier.build()),
                                Component.newline(),
                                Component.text("Status: " + status)
                        )
        );

        Component complete = identifier
                .hoverEvent(hover)
                .build();

        results[11] = Template.of("ticket", complete);

        return results;
    }

    /**
     * Convert the ticket instance into a json object with all data.
     *
     * @return JSON object
     */
    public @NonNull JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", this.id);

        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("name", this.userManager.getName(this.playerUUID));
        playerJson.addProperty("uuid", this.playerUUID.toString());
        json.add("player", playerJson);

        JsonObject locationJson = new JsonObject();
        locationJson.addProperty("world", this.location.getWorld());
        locationJson.addProperty("x", this.location.getX());
        locationJson.addProperty("y", this.location.getY());
        locationJson.addProperty("z", this.location.getZ());
        json.add("location", locationJson);

        json.addProperty("status", this.status.name());

        Optional<Message> lastNote = this.lastNote();
        Message currentMessage = this.currentMessage();

        json.addProperty("note", lastNote.map(Message::getData).orElse(null));
        json.addProperty("message", currentMessage.getData());

        return json;
    }

    @Override
    public boolean isDirty() {
        return this.dirty;
    }

}
