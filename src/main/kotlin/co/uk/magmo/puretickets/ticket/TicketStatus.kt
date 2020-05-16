package co.uk.magmo.puretickets.ticket

import org.bukkit.ChatColor

enum class TicketStatus(val color: ChatColor) {
    OPEN(ChatColor.GREEN), PICKED(ChatColor.YELLOW), CLOSED(ChatColor.RED);

    companion object {
        fun from(input: String): TicketStatus {
            return valueOf(input.toUpperCase())
        }
    }
}