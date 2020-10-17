package broccolai.tickets.utilities;

import broccolai.corn.core.Lists;
import broccolai.tickets.ticket.Message;
import broccolai.tickets.ticket.MessageReason;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import com.google.common.collect.Iterators;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ReplacementUtilities {

    private ReplacementUtilities() {
    }

    /**
     * Convert a ticket to an array of replacements
     *
     * @param ticket Ticket instance to use
     * @return Array of replacements
     */
    @NonNull
    public static String[] ticketReplacements(@Nullable final Ticket ticket) {
        List<String> results = new ArrayList<>();

        if (ticket == null) {
            return new String[0];
        }

        results.add("id");
        results.add(String.valueOf(ticket.getId()));

        Message message = ticket.currentMessage();

        results.add("ticket");
        results.add(message.getData());

        results.add("message");
        results.add(message.getData());

        results.add("messageDate");
        results.add(TimeUtilities.formatted(message.getDate()));

        results.add("statusColor");
        results.add(ticket.getStatus().getColor().toString());

        results.add("status");
        results.add(ticket.getStatus().name());

        results.add("player");
        results.add(UserUtilities.nameFromUUID(ticket.getPlayerUUID()));

        String picker;

        if (ticket.getStatus() == TicketStatus.PICKED) {
            picker = UserUtilities.nameFromUUID(ticket.getPickerUUID());
        } else {
            picker = "Unpicked";
        }

        results.add("picker");
        results.add(picker);

        Map<MessageReason, List<Message>> groupedMessages = Lists.group(ticket.getMessages(), Message::getReason);
        Message pickMessage = Iterators.getLast(groupedMessages.get(MessageReason.PICKED).iterator(), null);

        results.add("pickerDate");
        results.add(pickMessage != null ? TimeUtilities.formatted(pickMessage.getDate()) : "");

        results.add("date");
        results.add(TimeUtilities.formatted(ticket.dateOpened()));

        Message noteMessage = Iterators.getLast(groupedMessages.get(MessageReason.NOTE).iterator(), null);

        results.add("note");
        results.add(noteMessage != null ? noteMessage.getData() : "");

        Location location = ticket.getLocation();
        World world = location.getWorld();
        String worldName = world != null ? world.getName() : "world";

        results.add("world");
        results.add(worldName);

        results.add("x");
        results.add(String.valueOf(location.getBlockX()));

        results.add("y");
        results.add(String.valueOf(location.getBlockY()));

        results.add("z");
        results.add(String.valueOf(location.getBlockZ()));

        return results.toArray(new String[0]);
    }

}
