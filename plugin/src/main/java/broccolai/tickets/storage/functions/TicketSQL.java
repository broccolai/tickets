package broccolai.tickets.storage.functions;

import broccolai.corn.core.Lists;
import broccolai.tickets.storage.TimeAmount;
import broccolai.tickets.storage.platforms.Platform;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.utilities.generic.UserUtilities;
import cloud.commandframework.types.tuples.Pair;
import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import com.google.common.collect.ObjectArrays;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Ticket SQL.
 */
public class TicketSQL {
    private static Platform platform;

    /**
     * Initialise TicketSQL.
     *
     * @param platformInstance the platform instance
     */
    public static void setup(@NotNull Platform platformInstance) {
        platform = platformInstance;
    }

    /**
     * Retrieve a Ticket from the Database.
     *
     * @param id the tickets id
     * @return the constructed ticket
     */
    @Nullable
    public static Ticket select(int id) {
        DbRow row;

        try {
            row = DB.getFirstRow("SELECT id, uuid, status, picker, location from puretickets_ticket WHERE id = ?", id);
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return row != null ? HelpersSQL.buildTicket(row) : null;
    }

    /**
     * Retrieve a Ticket from the Database.
     *
     * @param id the tickets id
     * @return the constructed ticket
     */
    @Nullable
    public static Ticket select(int id, @NotNull UUID uuid) {
        DbRow row;

        try {
            row = DB.getFirstRow("SELECT id, uuid, status, picker, location from puretickets_ticket WHERE id = ? AND uuid = ?", id, uuid);
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return row != null ? HelpersSQL.buildTicket(row) : null;
    }

    /**
     * Retrieves tickets with the given optional status.
     *
     * @param status the optional status to filter with
     * @return a list of the retrieved tickets
     */
    @NotNull
    public static List<Ticket> selectAll(@Nullable TicketStatus status) {
        List<DbRow> results;

        try {
            if (status == null) {
                results = DB.getResults("SELECT id, uuid, status, picker, location FROM puretickets_ticket WHERE status <> ?", TicketStatus.CLOSED.name());
            } else {
                results = DB.getResults("SELECT id, uuid, status, picker, location FROM puretickets_ticket WHERE status = ?", status.name());
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return Lists.map(results, HelpersSQL::buildTicket);
    }

    /**
     * Retrieves tickets with a given players unique id and an optional status.
     *
     * @param uuid   the players unique id to filter with
     * @param status the optional status to filter with
     * @return a list of the retrieved tickets
     */
    @NotNull
    public static List<Ticket> selectAll(@NotNull UUID uuid, @Nullable TicketStatus status) {
        List<DbRow> results;

        @Language("SQL")
        String sql = "SELECT id, uuid, status, picker, location from puretickets_ticket WHERE uuid = ?";

        try {
            if (status == null) {
                results = DB.getResults(sql + " AND status <> ?", uuid.toString(), TicketStatus.CLOSED.name());
            } else {
                results = DB.getResults(sql + " AND status = ?", uuid.toString(), status.name());
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return Lists.map(results, HelpersSQL::buildTicket);
    }

    /**
     * Retrieves ticket ids with a given players unique id and an optional status.
     *
     * @param uuid     the players unique id to filter with
     * @param statuses the optional status to filter with
     * @return a list of the retrieved tickets
     */
    @NotNull
    public static List<Integer> selectIds(@NotNull UUID uuid, TicketStatus... statuses) {
        @Language("SQL")
        String sql = "SELECT id from puretickets_ticket WHERE uuid = ?";
        Pair<String, Object[]> extensions = buildWhereExtension(true, statuses);
        Object[] replacements = ObjectArrays.concat(uuid.toString(), extensions.getSecond());

        try {
            return DB.getFirstColumnResults(sql + extensions.getFirst(), replacements);
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Retrieve the last ticket with a players unique id and optionally multiple statues.
     *
     * @param uuid     the players unique id to filter with
     * @param statuses the optional statuses to filter with
     * @return the most recent ticket or if non are eligible null
     */
    @Nullable
    public static Ticket selectLastTicket(UUID uuid, TicketStatus... statuses) {
        @Language("SQL")
        String sql = "SELECT max(id) AS 'id', uuid, status, picker, location FROM puretickets_ticket WHERE uuid = ?";
        Pair<String, Object[]> extensions = buildWhereExtension(true, statuses);
        Object[] replacements = ObjectArrays.concat(uuid.toString(), extensions.getSecond());

        try {
            DbRow row = DB.getFirstRow(sql + extensions.getFirst(), replacements);
            return row.get("id") == null ? null : HelpersSQL.buildTicket(row);
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Retrieves the names of all ticket holders, optionally filtered by a status.
     *
     * @param statuses the optional statuses to filter by
     * @return a list of player names
     */
    @NotNull
    public static Set<String> selectNames(TicketStatus... statuses) {
        List<String> results;

        @Language("SQL")
        String sql = "SELECT uuid from puretickets_ticket";
        Pair<String, Object[]> extensions = buildWhereExtension(false, statuses);

        try {
            results = DB.getFirstColumnResults(sql + extensions.getFirst(), extensions.getSecond());
        } catch (final SQLException e) {
            throw new IllegalArgumentException();
        }

        return results.stream()
            .map(result -> UserUtilities.nameFromUUID(UUID.fromString(result)))
            .collect(Collectors.toSet());
    }

    /**
     * Retrieve a map of ticket stats, optionally filtered by a players unique id.
     *
     * @param uuid the optional players unique id to filter with
     * @return an enum map of the ticket stats
     */
    @NotNull
    public static EnumMap<TicketStatus, Integer> selectTicketStats(@Nullable UUID uuid) {
        DbRow row;

        @Language("SQL")
        String sql = "SELECT "
            + "SUM(Status LIKE 'OPEN') AS open, "
            + "SUM(Status LIKE 'PICKED') AS picked, "
            + "SUM(status LIKE 'CLOSED') AS closed "
            + "from puretickets_ticket ";

        try {
            if (uuid == null) {
                row = DB.getFirstRow(sql);
            } else {
                row = DB.getFirstRow(sql + " WHERE uuid = ?", uuid.toString());
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        EnumMap<TicketStatus, Integer> results = new EnumMap<>(TicketStatus.class);

        results.put(TicketStatus.OPEN, row.getInt("open"));
        results.put(TicketStatus.PICKED, row.getInt("picked"));
        results.put(TicketStatus.CLOSED, row.getInt("closed"));

        return results;
    }

    /**
     * Checks if a ticket exists using a given id.
     *
     * @param id the tickets id to filter with
     * @return true boolean
     */
    public static boolean exists(int id) {
        try {
            Integer value = DB.getFirstColumn("SELECT EXISTS(SELECT 1 from puretickets_ticket WHERE id = ?)", id);

            return value == 1;
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Count the amount of tickets with a given unique id and status.
     *
     * @param uuid   the players unique id to filter with
     * @param status the status to filter with
     * @return the number of tickets
     */
    public static int count(@NotNull UUID uuid, @NotNull TicketStatus status) {
        try {
            return platform.getPureInteger(DB.getFirstColumn("SELECT COUNT(id) FROM puretickets_ticket WHERE uuid = ? AND status = ?",
                uuid.toString(), status.name()));
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }


    /**
     * Count the amount of tickets with a given optional status.
     *
     * @param status the optional status to filter with
     * @return the number of tickets
     */
    public static int count(@Nullable TicketStatus status) {
        @Language("SQL")
        String sql = "SELECT COUNT(id) FROM puretickets_ticket";

        try {
            if (status == null) {
                return platform.getPureInteger(DB.getFirstColumn(sql));
            } else {
                return platform.getPureInteger(DB.getFirstColumn(sql + " WHERE status = ?", status.name()));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Insert a ticket into the Database and retrieve it's ticket id.
     *
     * @param uuid     the players unique id
     * @param status   the tickets status
     * @param picker   the pickers unique id
     * @param location the ticket creation location
     * @return the tickets id
     */
    public static int insert(@NotNull UUID uuid, @NotNull TicketStatus status, @Nullable UUID picker, @NotNull Location location) {
        Integer index;

        try {
            index = DB.getFirstColumn("SELECT max(id) from puretickets_ticket");

            if (index == null) {
                index = 1;
            } else {
                index += 1;
            }

            DB.executeInsert("INSERT INTO puretickets_ticket(id, uuid, status, picker, location) VALUES(?, ?, ?, ?, ?)",
                index, uuid.toString(), status.name(), picker, HelpersSQL.serializeLocation(location));
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return index;
    }

    /**
     * Updates a tickets entry in the Database.
     *
     * @param ticket the ticket to use
     */
    public static void update(@NotNull Ticket ticket) {
        UUID pickerUUID = ticket.getPickerUUID();
        String picker;

        if (pickerUUID == null) {
            picker = null;
        } else {
            picker = pickerUUID.toString();
        }

        DB.executeUpdateAsync("UPDATE puretickets_ticket SET status = ?, picker = ? WHERE id = ?",
            ticket.getStatus().name(), picker, ticket.getId());
    }

    /**
     * Retrieve the ticket completions grouped by player within an option time span.
     *
     * @param span the optional span to check within
     * @return a map of players and their ticket completions.
     */
    public static Map<UUID, Integer> highscores(@NotNull TimeAmount span) {
        Map<UUID, Integer> data = new HashMap<>();
        long length;

        if (span.getLength() == null) {
            length = 0;
        } else {
            length = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() - span.getLength();
        }

        @Language("SQL")
        String sql = "SELECT picker, COUNT(*) AS `num` "
            + "FROM puretickets_ticket "
            + "WHERE status = ? "
            + "AND picker IS NOT NULL "
            + "and id in (SELECT DISTINCT ticket FROM puretickets_message WHERE date > ?) "
            + "GROUP BY picker";

        List<DbRow> results;

        try {
            results = DB.getResults(sql, TicketStatus.CLOSED.name(), length);
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        results.forEach(result -> data.put(HelpersSQL.getUUID(result, "picker"), result.getInt("num")));

        return data;
    }

    @NotNull
    private static Pair<String, Object[]> buildWhereExtension(final boolean hasWhere, final TicketStatus... statuses) {
        final Object[] replacements = new String[statuses.length];
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < statuses.length; i++) {
            if (i == 0) {
                sb.append(hasWhere ? " AND (status = ?" : " WHERE status = ?");
            } else {
                sb.append(" OR status = ?");
            }

            replacements[i] = statuses[i].name();
        }

        if (hasWhere && statuses.length > 0) {
            sb.append(")");
        }

        return Pair.of(sb.toString(), replacements);
    }
}