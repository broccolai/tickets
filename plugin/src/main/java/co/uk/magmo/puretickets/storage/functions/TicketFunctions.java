package co.uk.magmo.puretickets.storage.functions;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import co.uk.magmo.puretickets.storage.TimeAmount;
import co.uk.magmo.puretickets.ticket.Ticket;
import co.uk.magmo.puretickets.ticket.TicketStatus;
import co.uk.magmo.puretickets.utilities.generic.ListUtilities;
import co.uk.magmo.puretickets.utilities.generic.UserUtilities;
import com.google.common.collect.ObjectArrays;
import org.bukkit.Location;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class TicketFunctions {
    private final HelpersSQL helpers;

    public TicketFunctions(HelpersSQL helpers) {
        this.helpers = helpers;
    }

    public Ticket select(Integer id) {
        DbRow row;

        try {
            row = DB.getFirstRow("SELECT id, uuid, status, picker, location from puretickets_ticket WHERE id = ?", id);
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return row != null ? helpers.buildTicket(row) : null;
    }

    public List<Ticket> selectAll(TicketStatus status) {
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

        return ListUtilities.map(results, helpers::buildTicket);
    }

    public List<Ticket> selectAll(UUID uuid, TicketStatus status) {
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

        return ListUtilities.map(results, helpers::buildTicket);
    }

    public List<Integer> selectIds(UUID uuid, TicketStatus status) {
        List<Integer> results;

        @Language("SQL")
        String sql = "SELECT id from puretickets_ticket WHERE uuid = ?";

        try {
            if (status == null) {
                results = DB.getFirstColumnResults(sql, uuid.toString());
            } else {
                results = DB.getFirstColumnResults(sql + " AND status = ?", uuid.toString(), status.name());
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return results;
    }

    public Ticket selectLastTicket(UUID uuid, TicketStatus... statuses) {
        ArrayList<String> replacements = new ArrayList<>();

        @Language("SQL")
        String sql = "SELECT max(id) AS 'id', uuid, status, picker, location FROM puretickets_ticket WHERE uuid = ?";

        for (int i = 0; i < statuses.length; i++) {
            if (i == 0) {
                sql += " AND status = ?";
            } else {
                sql += " OR status = ?";
            }

            replacements.add(statuses[i].name());
        }

        try {
            return helpers.buildTicket(DB.getFirstRow(sql, ObjectArrays.concat(uuid.toString(), replacements.toArray())));
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    public List<String> selectNames(TicketStatus status) {
        List<String> results;

        @Language("SQL")
        String sql = "SELECT uuid from puretickets_ticket";

        try {
            if (status == null) {
                results = DB.getFirstColumnResults(sql);
            } else {
                results = DB.getFirstColumnResults(sql + " WHERE status = ?", status.name());
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return ListUtilities.map(results, result -> {
            UUID uuid = UUID.fromString(result);
            return UserUtilities.nameFromUUID(uuid);
        });
    }

    public EnumMap<TicketStatus, Integer> selectTicketStats(UUID uuid) {
        DbRow row;

        @Language("SQL")
        String sql = "SELECT " +
                "SUM(Status LIKE 'OPEN') AS open, " +
                "SUM(Status LIKE 'PICKED') AS picked, " +
                "SUM(status LIKE 'CLOSED') AS closed " +
                "from puretickets_ticket ";

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

    public Boolean exists(Integer id) {
        try {
            Integer value = DB.getFirstColumn("SELECT EXISTS(SELECT 1 from puretickets_ticket WHERE id = ?)", id);

            return value == 1;
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    public Integer count(UUID uuid) {
        try {
            return DB.getFirstColumn("SELECT COUNT(id) FROM puretickets_ticket WHERE uuid = ?", uuid.toString());
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    public Integer count(TicketStatus status) {
        @Language("SQL")
        String sql = "SELECT COUNT(id) FROM puretickets_ticket";

        try {
            if (status == null) {
                return DB.getFirstColumn(sql);
            } else {
                return DB.getFirstColumn(sql + " WHERE status = ?", status.name());
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    public Integer insert(UUID uuid, TicketStatus status, UUID picker, Location location) {
        Integer index;

        try {
            index = DB.getFirstColumn("SELECT max(id) from puretickets_ticket");

            if (index == null) {
                index = 1;
            } else {
                index += 1;
            }

            DB.executeInsert("INSERT INTO puretickets_ticket(id, uuid, status, picker, location) VALUES(?, ?, ?, ?, ?)",
                    index, uuid.toString(), status.name(), picker, helpers.serializeLocation(location));
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return index;
    }

    public void update(Ticket ticket) {
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

    public HashMap<UUID, Integer> highscores(TimeAmount span) {
        HashMap<UUID, Integer> data = new HashMap<>();
        long length;

        if (span.getLength() == null) {
            length = 0;
        } else {
            length = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() - span.getLength();
        }

        @Language("SQL")
        String sql = "SELECT picker, COUNT(*) AS `num` " +
                "FROM puretickets_ticket " +
                "WHERE status = ? " +
                "AND picker IS NOT NULL " +
                "and id in (SELECT DISTINCT ticket FROM puretickets_message WHERE date > ?) " +
                "GROUP BY picker";

        List<DbRow> results;

        try {
            results = DB.getResults(sql, TicketStatus.CLOSED.name(), length);
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        results.forEach(result -> data.put(helpers.getUUID(result, "picker"), result.getInt("num")));

        return data;
    }
}