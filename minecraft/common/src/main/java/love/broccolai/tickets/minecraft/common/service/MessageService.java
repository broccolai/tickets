package love.broccolai.tickets.minecraft.common.service;

import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.minecraft.common.mooonshine.annotations.Receiver;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.annotation.Message;
import net.kyori.moonshine.annotation.Placeholder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MessageService {

    @Message("feedback.ticket.show")
    void showTicket(@Receiver Audience receiver, @Placeholder Ticket ticket);
}
