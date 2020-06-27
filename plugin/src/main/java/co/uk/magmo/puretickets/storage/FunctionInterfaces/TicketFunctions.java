package co.uk.magmo.puretickets.storage.FunctionInterfaces;

import co.uk.magmo.puretickets.interactions.PendingNotification;
import co.uk.magmo.puretickets.ticket.Message;
import co.uk.magmo.puretickets.ticket.Ticket;
import co.uk.magmo.puretickets.ticket.TicketStatus;
import com.google.common.collect.ArrayListMultimap;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface TicketFunctions {
    Ticket select(Integer id);

    List<Ticket> selectAll(TicketStatus status);

    List<Ticket> selectAll(UUID uuid, TicketStatus status);

    List<Integer> selectIds(UUID uuid, TicketStatus status);

    Integer selectHighestId(UUID uuid, TicketStatus... statuses);

    List<String> selectNames(TicketStatus status);

    HashMap<TicketStatus, Integer> selectTicketStats(UUID uuid);

    Boolean exists(Integer id);

    Integer count(TicketStatus status);

    Integer insert(UUID uuid, TicketStatus status, UUID picker, Location location);

    void update(Ticket ticket);
}