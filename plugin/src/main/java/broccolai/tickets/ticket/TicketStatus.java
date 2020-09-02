package broccolai.tickets.ticket;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * enum representing the potential states of a ticket.
 */
public enum TicketStatus {
    OPEN(ChatColor.GREEN), PICKED(ChatColor.YELLOW), CLOSED(ChatColor.RED);

    private final ChatColor color;

    TicketStatus(ChatColor color) {
        this.color = color;
    }

    /**
     * Retrieve the color associated with this status.
     *
     * @return the ChatColor
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * Retrieve a ticket status with a name.
     *
     * @param input the potential statuses name
     * @return the constructed status, or if not found null
     */
    public static TicketStatus from(@NotNull String input) {
        for (TicketStatus value : values()) {
            if (value.name().equals(input)) {
                return value;
            }
        }

        return null;
    }
}