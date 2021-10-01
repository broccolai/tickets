package broccolai.tickets.api.model.ticket;

import cloud.commandframework.arguments.flags.FlagContext;
import java.util.EnumSet;
import java.util.Set;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public enum TicketStatus {
    OPEN(TextColor.color(85, 255, 85)),
    CLAIMED(TextColor.color(255, 255, 85)),
    CLOSED(TextColor.color(255, 85, 85));

    private final TextColor color;

    TicketStatus(final @NonNull TextColor color) {
        this.color = color;
    }

    public @NonNull TextColor color() {
        return this.color;
    }

    public static @NonNull Set<TicketStatus> from(final @NonNull FlagContext flags) {
        TicketStatus status = flags.getValue("status", null);

        return status != null ? EnumSet.of(status) : EnumSet.of(OPEN, CLAIMED);
    }

}
