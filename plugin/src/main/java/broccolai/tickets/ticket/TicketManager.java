package broccolai.tickets.ticket;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.events.TicketConstructionEvent;
import broccolai.tickets.events.TicketCreationEvent;
import broccolai.tickets.exceptions.TooManyOpenTickets;
import broccolai.tickets.message.Message;
import broccolai.tickets.storage.SQLQueries;
import broccolai.tickets.storage.TimeAmount;
import broccolai.tickets.storage.mapper.TicketReducer;
import broccolai.tickets.tasks.TaskManager;
import broccolai.tickets.user.PlayerSoul;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class TicketManager implements Listener {

    private final Config config;
    private final PluginManager pluginManager;
    private final Jdbi jdbi;
    private final TicketIdStorage idStorage;
    private final Cache<Integer, Ticket> ticketCache;

    /**
     * Initialise a new Ticket Manager
     *
     * @param config        the config instance to use
     * @param pluginManager the pluginManager to call events with
     * @param jdbi          todo
     * @param taskManager   todo
     */
    public TicketManager(
            final @NonNull Config config,
            final @NonNull PluginManager pluginManager,
            final @NonNull Jdbi jdbi,
            final @NonNull TaskManager taskManager
    ) {
        this.config = config;
        this.pluginManager = pluginManager;
        this.jdbi = jdbi;
        this.idStorage = new TicketIdStorage(jdbi);

        this.ticketCache = CacheBuilder.newBuilder()
                .maximumSize(500)
                .expireAfterAccess(60, TimeUnit.MINUTES)
                .removalListener(new TicketRemovalListener(taskManager, this))
                .build();
    }

    /**
     * Get an optional ticket
     *
     * @param id Tickets id
     * @return Optional ticket
     */
    public @NonNull Optional<Ticket> getTicket(final int id) {
        try {
            Ticket ticket = ticketCache.get(id, () -> jdbi.withHandle(handle -> {
                return handle.createQuery(SQLQueries.SELECT_TICKET.get())
                        .bind("id", id)
                        .reduceRows(new TicketReducer())
                        .findFirst()
                        .orElseThrow(Exception::new);
            }));

            return Optional.of(ticket);
        } catch (ExecutionException e) {
            return Optional.empty();
        }
    }

    /**
     * Get the most recent (highest id) ticket, filtered with a uuid and ticket statuses
     *
     * @param uuid     Unique id
     * @param statuses Statuses to filter with
     * @return Optional ticket
     */
    public @NonNull Optional<Ticket> getRecentTicket(final @NonNull UUID uuid, final @NonNull TicketStatus... statuses) {
        try {
            int id = jdbi.withHandle(handle -> {
                return handle.createQuery(SQLQueries.SELECT_HIGHEST_ID_WHERE.get())
                        .bind("uuid", uuid)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElseThrow(Exception::new);
            });

            return getTicket(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Get tickets, filtered with ticket statuses
     *
     * @param statuses Statuses to filter with
     * @return List of tickets
     */
    public @NonNull List<Ticket> getTickets(final TicketStatus... statuses) {
        TicketStatus[] toBind = statuses.length != 0 ? statuses : TicketStatus.values();

        List<Integer> ids = jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.SELECT_IDS_STATUS.get())
                    .bindList("statuses", Arrays.asList(toBind))
                    .mapTo(Integer.class)
                    .list();
        });

        return getTickets(ids.toArray(new Integer[0]));
    }

    /**
     * Get tickets, filtered with a unique id and ticket statuses
     *
     * @param uuid     Unique id
     * @param statuses Statuses to filter with
     * @return List of tickets
     */
    public @NonNull List<Ticket> getTickets(final @NonNull UUID uuid, final @NonNull TicketStatus... statuses) {
        TicketStatus[] toBind = statuses.length != 0 ? statuses : TicketStatus.values();

        List<Integer> ids = jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.SELECT_IDS_UUID_STATUS.get())
                    .bind("uuid", uuid.toString())
                    .bindList("statuses", Arrays.asList(statuses))
                    .mapTo(Integer.class)
                    .list();
        });

        return getTickets(ids.toArray(new Integer[0]));
    }

    /**
     * Get tickets using ids
     *
     * @param ids Ticket ids
     * @return List of tickets
     */
    public @NonNull List<Ticket> getTickets(final Integer... ids) {
        List<Ticket> tickets = new ArrayList<>();
        List<Integer> toQuery = new ArrayList<>();

        for (int id : ids) {
            Ticket ticket = ticketCache.getIfPresent(id);

            if (ticket == null) {
                toQuery.add(id);
                continue;
            }

            tickets.add(ticket);
        }

        if (!toQuery.isEmpty()) {
            List<Ticket> queriedTickets = jdbi.withHandle(handle -> {
                return handle.createQuery(SQLQueries.SELECT_TICKETS.get())
                        .bindList("ids", toQuery)
                        .reduceRows(new TicketReducer())
                        .collect(Collectors.toList());
            });


            tickets.addAll(queriedTickets);
        }

        return tickets;
    }

    /**
     * Count tickets filtered by their status
     *
     * @param statuses Statuses to filter with
     * @return Count of filtered tickets
     */
    public int countTickets(final @NonNull TicketStatus... statuses) {
        return jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.COUNT_TICKETS.get())
                    .bindList("statuses", Arrays.asList(statuses))
                    .mapTo(Integer.class)
                    .first();
        });
    }

    /**
     * Count tickets filtered by their unique id and their status
     *
     * @param uuid     Unique id
     * @param statuses Statuses to filter with
     * @return Count of filtered tickets
     */
    public int countTickets(final @NonNull UUID uuid, final @NonNull TicketStatus... statuses) {
        return jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.COUNT_TICKETS_UUID.get())
                    .bind("uuid", uuid)
                    .bindList("statuses", Arrays.asList(statuses))
                    .mapTo(Integer.class)
                    .first();
        });
    }

    /**
     * Get the current ticket stats
     *
     * @return Ticket stats
     */
    public TicketStats getStats() {
        return jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.SELECT_TICKET_STATS.get())
                    .mapTo(TicketStats.class)
                    .first();
        });
    }

    /**
     * Get the current ticket stats filtered by a unique id
     *
     * @param uuid Unique id
     * @return Ticket sats
     */
    public TicketStats getStats(final @NonNull UUID uuid) {
        return jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.SELECT_TICKET_STATS_UUID.get())
                    .bind("uuid", uuid)
                    .mapTo(TicketStats.class)
                    .first();
        });
    }

    /**
     * Get highscores by a time amount
     *
     * @param span Time span to get highscores against
     * @return Map of unique id's and their score
     */
    public Map<UUID, Integer> getHighscores(final @NonNull TimeAmount span) {
        long length = span.getLength() != null
                ? LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() - span.getLength()
                : 0;

        return jdbi.withHandle(handle -> {
            return handle.createQuery(SQLQueries.SELECT_HIGHSCORES.get())
                    .bind("status", TicketStatus.CLOSED)
                    .bind("date", length)
                    .reduceRows(new HashMap<>(), (map, rowView) -> {
                        map.put(
                                rowView.getColumn("picker", UUID.class),
                                rowView.getColumn("num", Integer.class)
                        );

                        return map;
                    });
        });
    }

    /**
     * Insert a ticket to storage using a uuid and a location
     *
     * @param uuid     Unique id
     * @param location Tickets location
     * @return Tickets id
     */
    public int insertTicket(final @NonNull UUID uuid, final @NonNull Location location) {
        return jdbi.withHandle(handle -> {
            int id = handle.createQuery(SQLQueries.SELECT_HIGHEST_ID.get())
                    .mapTo(Integer.class)
                    .findFirst()
                    .orElse(1);

            World world = location.getWorld();
            String locationSerialised = (world != null
                    ? world.getName()
                    : "null") + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ();

            handle.createUpdate(SQLQueries.INSERT_TICKET.get())
                    .bind("id", id)
                    .bind("uuid", uuid)
                    .bind("status", TicketStatus.OPEN.name())
                    .bind("picker", (UUID) null)
                    .bind("location", locationSerialised)
                    .execute();

            return id;
        });
    }

    /**
     * Update a dirty tickets information in storage
     *
     * @param ticket Ticket
     */
    public void updateTicket(final @NonNull Ticket ticket) {
        jdbi.useHandle(handle -> {
            handle.createUpdate(SQLQueries.UPDATE_TICKET.get())
                    .bind("id", ticket.getId())
                    .bind("status", ticket.getStatus())
                    .bind("picker", ticket.getPickerUUID())
                    .execute();
        });
    }

    /**
     * Insert a ticket's message into storage
     *
     * @param ticketId Tickets id to add against
     * @param message  Message
     */
    public void insertMessage(final int ticketId, final @NonNull Message message) {
        jdbi.useHandle(handle -> {
            handle.createUpdate(SQLQueries.INSERT_MESSAGE.get())
                    .bind("ticket", ticketId)
                    .bind("reason", message.getReason())
                    .bind("data", message.getData())
                    .bind("sender", message.getSender())
                    .bind("date", message.getDate())
                    .execute();
        });
    }

    /**
     * Get the id storage
     *
     * @return Id storage
     */
    public TicketIdStorage getIdStorage() {
        return idStorage;
    }

    private @NonNull String joinSetToInt(final @NonNull Set<Integer> input) {
        StringBuilder sb = new StringBuilder();

        for (int num : input) {
            sb.append(num);
            sb.append(",");
        }

        return sb.toString();
    }

    /**
     * Listener to check construction
     *
     * @param e Event
     */
    @EventHandler
    public void onTicketConstructPredicates(final @NonNull TicketConstructionEvent e) {
        PlayerSoul soul = e.getSoul();

        if (this.countTickets(soul.getUniqueId(), TicketStatus.OPEN) > config.getTicketLimitOpen() + 1) {
            e.cancel(new TooManyOpenTickets(config));
        }
    }

    /**
     * Listener to construct ticket after modifications
     *
     * @param e Event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTicketConstruct(final @NonNull TicketConstructionEvent e) {
        PlayerSoul soul = e.getSoul();
        Message message = e.getMessage();

        UUID uuid = soul.getUniqueId();
        Location location = soul.asPlayer().getLocation();

        int id = this.insertTicket(uuid, location);
        Ticket ticket = new Ticket(id, uuid, location, TicketStatus.OPEN, null);

        ticket.withMessage(message);
        this.insertMessage(id, message);

        TicketCreationEvent creationEvent = new TicketCreationEvent(soul, ticket);
        pluginManager.callEvent(creationEvent);
    }

}
