package broccolai.tickets.api.service.message;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.message.moonshine.Causer;
import broccolai.tickets.api.service.message.moonshine.Receiver;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.annotation.Message;
import net.kyori.moonshine.annotation.Placeholder;

public interface MessageService {

    @Message("feedback.create")
    void feedbackCreate(@Receiver Audience receiver, @Placeholder Ticket ticket);

    @Message("announce.creation")
    void announceCreation(@Causer Soul causer, @Placeholder Ticket ticket);

}
