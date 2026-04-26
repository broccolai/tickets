package love.broccolai.tickets.minecraft.common.service;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketDataField(
    String identifier,
    String label,
    Object value
) {
}
