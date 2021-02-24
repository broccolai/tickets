package broccolai.tickets.core.utilities;

import broccolai.tickets.api.model.ticket.TicketStatus;
import cloud.commandframework.arguments.flags.FlagContext;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class FlagsUtilities {

    private FlagsUtilities() {
    }

    public static @NonNull TicketStatus[] fromFlags(final @NonNull FlagContext flags) {
        TicketStatus status = flags.getValue("status", null);

        return status != null ? of(status) : of(TicketStatus.OPEN, TicketStatus.PICKED);
    }

    public static @NonNull TicketStatus[] of(final @NonNull TicketStatus... values) {
        return values;
    }
}
