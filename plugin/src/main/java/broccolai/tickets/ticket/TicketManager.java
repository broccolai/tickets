package broccolai.tickets.ticket;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.events.TicketConstructionEvent;
import broccolai.tickets.events.TicketCreationEvent;
import broccolai.tickets.exceptions.TicketClosed;
import broccolai.tickets.exceptions.TicketOpen;
import broccolai.tickets.exceptions.TooManyOpenTickets;
import broccolai.tickets.storage.functions.MessageSQL;
import broccolai.tickets.storage.functions.TicketSQL;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manager for Ticket interaction.
 */
public class TicketManager implements Listener {
    @NotNull
    private final Config config;
    @NotNull
    private final PluginManager pluginManager;

    /**
     * Initialise a new Ticket Manager.
     *
     * @param config        the config instance to use
     * @param pluginManager the pluginManager to call events with
     */
    public TicketManager(@NotNull Config config, @NotNull PluginManager pluginManager) {
        this.config = config;
        this.pluginManager = pluginManager;
    }

    /**
     * Adds a new message to a ticket instance.
     *
     * @param ticket  the ticket instance to modify
     * @param message the message to add to the ticket
     * @return the modified ticket
     * @throws TicketClosed if the ticket is closed
     */
    public Ticket update(@NotNull Ticket ticket, @NotNull Message message) throws TicketClosed {
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new TicketClosed();
        }

        return addMessageAndUpdate(ticket, message);
    }

    /**
     * Picks a ticket using the supplied actioners unique id.
     *
     * @param uuid   the actioners unique id
     * @param ticket the ticket instance to modify
     * @return the modified ticket
     * @throws TicketClosed if the ticket is closed
     */
    public Ticket pick(@Nullable UUID uuid, @NotNull Ticket ticket) throws TicketClosed {
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new TicketClosed();
        }

        Message message = new Message(MessageReason.PICKED, LocalDateTime.now(), uuid);

        ticket.setStatus(TicketStatus.PICKED);
        ticket.setPickerUUID(uuid);

        return addMessageAndUpdate(ticket, message);
    }

    /**
     * Yields a ticket using the supplied actioners unique id.
     *
     * @param uuid   the actioners unique id
     * @param ticket the ticket instance to modify
     * @return the modified ticket
     * @throws TicketOpen if the ticket is open
     */
    public Ticket yield(@Nullable UUID uuid, @NotNull Ticket ticket) throws TicketOpen {
        if (ticket.getStatus() == TicketStatus.OPEN) {
            throw new TicketOpen();
        }

        Message message = new Message(MessageReason.YIELDED, LocalDateTime.now(), uuid);

        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPickerUUID(uuid);

        return addMessageAndUpdate(ticket, message);
    }

    /**
     * Closes a ticket using the supplied actioners unique id.
     *
     * @param uuid   the actioners unique id
     * @param ticket the ticket instance to modify
     * @return the modified ticket
     * @throws TicketClosed if the ticket is already closed
     */
    public Ticket close(@Nullable UUID uuid, @NotNull Ticket ticket) throws TicketClosed {
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new TicketClosed();
        }

        Message message = new Message(MessageReason.CLOSED, LocalDateTime.now(), uuid);

        ticket.setStatus(TicketStatus.CLOSED);

        return addMessageAndUpdate(ticket, message);
    }

    /**
     * Done-marks a ticket using the supplied actioners unique id.
     *
     * @param uuid   the actioners unique id
     * @param ticket the ticket instance to modify
     * @return the modified ticket
     * @throws TicketClosed if the ticket is already closed
     */
    public Ticket done(@Nullable UUID uuid, @NotNull Ticket ticket) throws TicketClosed {
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new TicketClosed();
        }

        Message message = new Message(MessageReason.DONE_MARKED, LocalDateTime.now(), uuid);

        ticket.setStatus(TicketStatus.CLOSED);

        return addMessageAndUpdate(ticket, message);
    }

    /**
     * Opens a ticket using the supplied actioners unique id.
     *
     * @param uuid   the actioners unique id
     * @param ticket the ticket instance to modify
     * @return the modified ticket
     * @throws TicketOpen if the ticket is already open
     */
    public Ticket reopen(@Nullable UUID uuid, @NotNull Ticket ticket) throws TicketOpen {
        if (ticket.getStatus() == TicketStatus.OPEN) {
            throw new TicketOpen();
        }

        Message message = new Message(MessageReason.REOPENED, LocalDateTime.now(), uuid);

        ticket.setStatus(TicketStatus.OPEN);

        return addMessageAndUpdate(ticket, message);
    }

    /**
     * Adds a note to a ticket using the supplied actioners unique id.
     *
     * @param uuid   the actioners unique id
     * @param ticket the ticket instance to modify
     * @param input  the note message
     * @return the modified ticket
     */
    public Ticket note(@Nullable UUID uuid, @NotNull Ticket ticket, @NotNull String input) {
        Message message = new Message(MessageReason.NOTE, LocalDateTime.now(), input, uuid);

        return addMessageAndUpdate(ticket, message);
    }

    /**
     * Add a message to a ticket and update the Database.
     *
     * @param ticket  the ticket instance to modify
     * @param message the message to add
     * @return the modified ticket
     */
    private Ticket addMessageAndUpdate(@NotNull Ticket ticket, @NotNull Message message) {
        ticket.getMessages().add(message);

        MessageSQL.insert(ticket, message);
        TicketSQL.update(ticket);

        return ticket;
    }

    /**
     * Listener to check construction.
     */
    @EventHandler
    public void onTicketConstructPredicates(TicketConstructionEvent e) {
        Player player = e.getPlayer();

        if (TicketSQL.count(player.getUniqueId(), TicketStatus.OPEN) > config.LIMIT__OPEN_TICKETS + 1) {
            e.cancel(new TooManyOpenTickets(config));
        }
    }

    /**
     * Listener to construct ticket after modifications.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTicketConstruct(TicketConstructionEvent e) {
        Player player = e.getPlayer();
        Message message = e.getMessage();

        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();

        int id = TicketSQL.insert(uuid, TicketStatus.OPEN, null, location);
        Ticket ticket = new Ticket(id, uuid, Lists.newArrayList(message), location, TicketStatus.OPEN, null);

        MessageSQL.insert(ticket, message);

        TicketCreationEvent creationEvent = new TicketCreationEvent(player, ticket);
        pluginManager.callEvent(creationEvent);
    }
}