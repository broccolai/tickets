package co.uk.magmo.puretickets.ticket;

import co.uk.magmo.puretickets.storage.SQLManager;
import co.uk.magmo.puretickets.storage.TimeAmount;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.*;

public class TicketManager {
    private final SQLManager sqlManager;

    public TicketManager(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public @Nullable Ticket get(int id) {
        return sqlManager.getTicket().select(id);
    }

    public List<Ticket> getAll(UUID uuid, TicketStatus status) {
        return sqlManager.getTicket().selectAll(uuid, status);
    }

    public Ticket getLatestTicket(UUID uuid, TicketStatus... statuses) {
        return sqlManager.getTicket().selectLastTicket(uuid, statuses);
    }

    public List<Integer> getIds(UUID uuid, TicketStatus status) {
        return sqlManager.getTicket().selectIds(uuid, status);
    }

    public Boolean exists(Integer id) {
        return sqlManager.getTicket().exists(id);
    }

    public Integer count(TicketStatus status) {
        return sqlManager.getTicket().count(status);
    }

    public EnumMap<TicketStatus, Integer> stats(UUID uuid) {
        return sqlManager.getTicket().selectTicketStats(uuid);
    }

    public List<Ticket> all(TicketStatus status) {
        return sqlManager.getTicket().selectAll(status);
    }

    public List<String> allNames(TicketStatus status) {
        return sqlManager.getTicket().selectNames(status);
    }

    public HashMap<UUID, Integer> highscores(TimeAmount span) {
        return sqlManager.getTicket().highscores(span);
    }

    public Ticket createTicket(Player player, Message message) {
        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();

        Integer id = sqlManager.getTicket().insert(uuid, TicketStatus.OPEN, null, location);
        Ticket ticket = new Ticket(id, uuid, Lists.newArrayList(message), location, TicketStatus.OPEN, null);

        sqlManager.getMessage().insert(ticket, message);

        return ticket;
    }

    public Ticket update(Integer id, Message message) {
        return addMessageAndUpdate(get(id), message);
    }

    public Ticket pick(UUID uuid, Integer id) {
        Ticket ticket = get(id);
        Message message = new Message(MessageReason.PICKED, LocalDateTime.now(), uuid);

        ticket.setStatus(TicketStatus.PICKED);
        ticket.setPickerUUID(uuid);

        return addMessageAndUpdate(ticket, message);
    }

    public Ticket yield(UUID uuid, Integer id) {
        Ticket ticket = get(id);
        Message message = new Message(MessageReason.REOPENED, LocalDateTime.now(), uuid);

        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPickerUUID(uuid);

        return addMessageAndUpdate(ticket, message);
    }

    public Ticket close(UUID uuid, Integer id) {
        Ticket ticket = get(id);
        Message message = new Message(MessageReason.CLOSED, LocalDateTime.now(), uuid);

        ticket.setStatus(TicketStatus.CLOSED);

        return addMessageAndUpdate(ticket, message);
    }

    public Ticket reopen(UUID uuid, Integer id) {
        Ticket ticket = get(id);
        Message message = new Message(MessageReason.REOPENED, LocalDateTime.now(), uuid);

        ticket.setStatus(TicketStatus.OPEN);

        return addMessageAndUpdate(ticket, message);
    }

    public Ticket note(UUID uuid, Integer id, String input) {
        Ticket ticket = get(id);
        Message message = new Message(MessageReason.NOTE, LocalDateTime.now(), input, uuid);

        return addMessageAndUpdate(ticket, message);
    }

    private Ticket addMessageAndUpdate(Ticket ticket, Message message) {
        ticket.getMessages().add(message);

        sqlManager.getMessage().insert(ticket, message);
        sqlManager.getTicket().update(ticket);

        return ticket;
    }
}