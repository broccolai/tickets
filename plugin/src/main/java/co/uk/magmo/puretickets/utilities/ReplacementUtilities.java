package co.uk.magmo.puretickets.utilities;

import co.uk.magmo.puretickets.ticket.Message;
import co.uk.magmo.puretickets.ticket.MessageReason;
import co.uk.magmo.puretickets.ticket.Ticket;
import co.uk.magmo.puretickets.ticket.TicketStatus;

import java.util.ArrayList;

public class ReplacementUtilities {
    public static String[] ticketReplacements(Ticket ticket) {
        ArrayList<String> results = new ArrayList<>();

        if (ticket == null) return new String[0];

        results.add("%id%");
        results.add(ticket.getId().toString());

        String message = ticket.currentMessage().getData();

        results.add("%ticket%");
        results.add(message);
        results.add("%message%");
        results.add(message);

        results.add("%statusColor%");
        results.add(ticket.getStatus().getPureColor().getColor().toString());

        results.add("%status%");
        results.add(ticket.getStatus().name());

        String picker;

        if (ticket.getStatus() == TicketStatus.PICKED) {
            picker = UserUtilities.nameFromUUID(ticket.getPickerUUID());
        } else {
            picker = "Unpicked";
        }

        results.add("%picker%");
        results.add(picker);

        results.add("%date%");
        results.add(TimeUtilities.formatted(ticket.dateOpened()));

        String note = "";

        for (Message msg : ticket.getMessages()) {
            if (msg.getReason() == MessageReason.NOTE) {
                note = msg.getData();
                break;
            }
        }

        results.add("%note%");
        results.add(note);

        return results.toArray(new String[0]);
    }
}
