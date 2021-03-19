package broccolai.tickets.api.model.ticket;

import cloud.commandframework.arguments.flags.FlagContext;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.EnumSet;
import java.util.Set;

/**
 * enum representing the potential states of a ticket
 */
public enum TicketStatus {
    OPEN(TextColor.color(85, 255, 85)),
    CLAIMED(TextColor.color(255, 255, 85)),
    CLOSED(TextColor.color(255, 85, 85));

    private final TextColor color;

    TicketStatus(final TextColor color) {
        this.color = color;
    }

    /**
     * Retrieve the color associated with this status
     */
    public TextColor color() {
        return this.color;
    }

    /**
     * Retrieve a ticket status with a name
     */
    public static @Nullable TicketStatus from(final @NonNull String input) {
        for (TicketStatus value : values()) {
            if (value.name().equalsIgnoreCase(input)) {
                return value;
            }
        }

        return null;
    }

    /**
     * Retrieve an array of statuses from a flag context
     */
    public static @NonNull Set<TicketStatus> from(final @NonNull FlagContext flags) {
        TicketStatus status = flags.getValue("status", null);

        return status != null ? EnumSet.of(status) : EnumSet.of(OPEN, CLAIMED);
    }

}
