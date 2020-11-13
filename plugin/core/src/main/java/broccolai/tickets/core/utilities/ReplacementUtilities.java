package broccolai.tickets.core.utilities;

import broccolai.corn.core.Lists;
import broccolai.tickets.core.message.Message;
import broccolai.tickets.core.message.MessageReason;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.ticket.TicketStatus;
import com.google.common.collect.Iterators;
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
    public static @NonNull String[] ticketReplacements(final @Nullable Ticket ticket) {
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
        results.add(ticket.getStatus().getColor().asHexString());

        results.add("status");
        results.add(ticket.getStatus().name());

        results.add("player");
//        results.add(UserUtilities.nameFromUUID(ticket.getPlayerUUID()));
        results.add("todo");

        String picker;

        if (ticket.getStatus() == TicketStatus.PICKED) {
//            picker = UserUtilities.nameFromUUID(ticket.getPickerUUID());
            picker = "todo";
        } else {
            picker = "Unpicked";
        }

        results.add("picker");
        results.add(picker);

        Map<MessageReason, List<Message>> groupedMessages = Lists.group(ticket.getMessages(), Message::getReason);

        List<Message> pickList = groupedMessages.get(MessageReason.PICKED);
        String pickData = "";

        if (pickList != null) {
            Message pickMessage = Iterators.getLast(pickList.iterator(), null);
            pickData = pickMessage != null ? TimeUtilities.formatted(pickMessage.getDate()) : pickData;
        }

        results.add("pickerDate");
        results.add(pickData);

        results.add("date");
        results.add(TimeUtilities.formatted(ticket.dateOpened()));

        List<Message> noteList = groupedMessages.get(MessageReason.NOTE);
        String noteData = "";

        if (noteList != null) {
            Message noteMessage = Iterators.getLast(noteList.iterator(), null);
            noteData = noteMessage != null ? noteMessage.getData() : noteData;
        }

        results.add("note");
        results.add(noteData);

        TicketLocation location = ticket.getLocation();
        String worldRaw = location.getWorld();
        String worldName = worldRaw != null ? worldRaw : "world";

        results.add("world");
        results.add(worldName);

        results.add("x");
        results.add(String.valueOf(location.getX()));

        results.add("y");
        results.add(String.valueOf(location.getY()));

        results.add("z");
        results.add(String.valueOf(location.getZ()));

        return results.toArray(new String[0]);
    }

}
