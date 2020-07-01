package co.uk.magmo.puretickets.utilities;

import co.uk.magmo.puretickets.ticket.Message;
import co.uk.magmo.puretickets.ticket.MessageReason;
import co.uk.magmo.puretickets.ticket.Ticket;
import co.uk.magmo.puretickets.ticket.TicketStatus;
import com.google.common.collect.Iterators;
import java.util.ArrayList;
import java.util.List;

public class ReplacementUtilities {
    public static String[] ticketReplacements(Ticket ticket) {
        ArrayList<String> results = new ArrayList<>();

        if (ticket == null) return new String[0];

        results.add("%id%");
        results.add(ticket.getId().toString());

        Message message = ticket.currentMessage();

        results.add("%ticket%");
        results.add(message.getData());

        results.add("%message%");
        results.add(message.getData());

        results.add("%messageDate%");
        results.add(TimeUtilities.formatted(message.getDate()));

        results.add("%statusColor%");
        results.add(ticket.getStatus().getPureColor().getColor().toString());

        results.add("%status%");
        results.add(ticket.getStatus().name());

        results.add("%player%");
        results.add(UserUtilities.nameFromUUID(ticket.getPlayerUUID()));

        String picker;

        if (ticket.getStatus() == TicketStatus.PICKED) {
            picker = UserUtilities.nameFromUUID(ticket.getPickerUUID());
        } else {
            picker = "Unpicked";
        }

        results.add("%picker%");
        results.add(picker);

        List<Message> pickMessages = ListUtilities.filter(ticket.getMessages(), msg -> msg.getReason() == MessageReason.PICKED);
        Message pickMessage = Iterators.getLast(pickMessages.iterator(), null);

        results.add("pickerDate");
        results.add(pickMessage != null ? TimeUtilities.formatted(pickMessage.getDate()) : "");

        results.add("%date%");
        results.add(TimeUtilities.formatted(ticket.dateOpened()));

        List<Message> noteMessages = ListUtilities.filter(ticket.getMessages(), msg -> msg.getReason() == MessageReason.NOTE);
        Message noteMessage = Iterators.getLast(noteMessages.iterator(), null);

        results.add("%note%");
        results.add(noteMessage != null ? noteMessage.getData() : "");

        return results.toArray(new String[0]);
    }
}
