package broccolai.tickets.ticket;

import org.bukkit.ChatColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * enum representing the potential states of a ticket
 */
public enum TicketStatus {
    OPEN(ChatColor.GREEN),
    PICKED(ChatColor.YELLOW),
    CLOSED(ChatColor.RED);

    @NonNull
    private final ChatColor color;

    TicketStatus(@NonNull final ChatColor color) {
        this.color = color;
    }

    /**
     * Retrieve the color associated with this status
     *
     * @return the ChatColor
     */
    @NonNull
    public ChatColor getColor() {
        return color;
    }

    /**
     * Retrieve a ticket status with a name
     *
     * @param input the potential statuses name
     * @return the constructed status, or if not found null
     */
    @Nullable
    public static TicketStatus from(@NonNull final String input) {
        for (TicketStatus value : values()) {
            if (value.name().equalsIgnoreCase(input)) {
                return value;
            }
        }

        return null;
    }
}
