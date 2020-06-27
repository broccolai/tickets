package co.uk.magmo.puretickets.storage.MySQL;

import co.uk.magmo.puretickets.storage.FunctionInterfaces.MessageFunctions;
import co.uk.magmo.puretickets.storage.FunctionInterfaces.MiscellaneousFunctions;
import co.uk.magmo.puretickets.storage.FunctionInterfaces.TicketFunctions;
import co.uk.magmo.puretickets.storage.SQLManager;
import co.uk.magmo.puretickets.storage.TimeAmount;
import co.uk.magmo.puretickets.ticket.Message;
import co.uk.magmo.puretickets.ticket.Ticket;
import co.uk.magmo.puretickets.ticket.TicketStatus;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MySQLManager extends SQLManager {
    public MySQLManager() {
        super(new MiscellaneousImpl(), );
    }

    private static class MiscellaneousImpl implements MiscellaneousFunctions {
        @Override
        public void setup(Plugin plugin) {

        }

        @Override
        public HashMap<UUID, Integer> highscores(TimeAmount amount) {
            return null;
        }
    }

    private static class TicketImpl implements TicketFunctions {
        @Override
        public co.uk.magmo.puretickets.ticket.Ticket select(Integer id) {
            return null;
        }

        @Override
        public List<co.uk.magmo.puretickets.ticket.Ticket> selectAll(TicketStatus status) {
            return null;
        }

        @Override
        public List<co.uk.magmo.puretickets.ticket.Ticket> selectAll(UUID uuid, TicketStatus status) {
            return null;
        }

        @Override
        public List<Integer> selectIds(UUID uuid, TicketStatus status) {
            return null;
        }

        @Override
        public Integer selectHighestId(UUID uuid, TicketStatus... statuses) {
            return null;
        }

        @Override
        public List<String> selectNames(TicketStatus status) {
            return null;
        }

        @Override
        public HashMap<TicketStatus, Integer> selectTicketStats(UUID uuid) {
            return null;
        }

        @Override
        public Boolean exists(Integer id) {
            return null;
        }

        @Override
        public Integer count(TicketStatus status) {
            return null;
        }

        @Override
        public Integer insert(UUID uuid, TicketStatus status, UUID picker, Location location) {
            return null;
        }

        @Override
        public void update(co.uk.magmo.puretickets.ticket.Ticket ticket) {

        }
    }

    private static class MessageImpl implements MessageFunctions {
        @Override
        public ArrayList<co.uk.magmo.puretickets.ticket.Message> selectAll(Integer id) {
            return null;
        }

        @Override
        public void insert(co.uk.magmo.puretickets.ticket.Ticket ticket, co.uk.magmo.puretickets.ticket.Message message) {

        }
    }
}
