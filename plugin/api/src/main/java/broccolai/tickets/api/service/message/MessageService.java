package broccolai.tickets.api.service.message;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.message.moonshine.Causer;
import broccolai.tickets.api.service.message.moonshine.Receiver;
import broccolai.tickets.api.service.message.moonshine.StaffReceiver;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.annotation.Message;
import net.kyori.moonshine.annotation.Placeholder;

public interface MessageService {

    @Message("feedback.create")
    void feedbackCreate(@Receiver Audience receiver, @Placeholder Ticket ticket);

    @Message("feedback.reopen")
    void feedbackReopen(@Receiver Audience receiver, @Placeholder Ticket ticket);

    @Message("feedback.update")
    void feedbackUpdate(@Receiver Audience receiver, @Placeholder Ticket ticket);

    @Message("feedback.close")
    void feedbackClose(@Receiver Audience receiver, @Placeholder Ticket ticket);

    @Message("feedback.claim")
    void feedbackClaim(@Receiver Audience receiver, @Placeholder Ticket ticket);

    @Message("feedback.unclaim")
    void feedbackUnclaim(@Receiver Audience receiver, @Placeholder Ticket ticket);

    @Message("feedback.assign")
    void feedbackAssign(@Receiver Audience receiver, @Placeholder Soul target, @Placeholder Ticket ticket);

    @Message("feedback.complete")
    void feedbackComplete(@Receiver Audience receiver, @Placeholder Ticket ticket);

    @Message("feedback.note")
    void feedbackNote(@Receiver Audience receiver, @Placeholder Ticket ticket);

    @Message("notify.claim")
    void notifyClaim(@Receiver @Placeholder Ticket ticket, @Placeholder Soul soul);

    @Message("notify.unclaim")
    void notifyUnclaim(@Receiver @Placeholder Ticket ticket, @Placeholder Soul soul);

    @Message("notify.reopen")
    void notifyReopen(@Receiver @Placeholder Ticket ticket, @Placeholder Soul soul);

    @Message("notify.complete")
    void notifyComplete(@Receiver @Placeholder Ticket ticket, @Placeholder Soul soul);

    @Message("notify.note")
    void notifyNote(@Receiver @Placeholder Ticket ticket, @Placeholder String note, @Placeholder Soul soul);

    @Message("notify.assign")
    void notifyAssign(@Receiver Soul soul, @Placeholder Ticket ticket);

    @StaffReceiver
    @Message("announce.creation")
    void announceCreation(@Causer @Placeholder Soul causer, @Placeholder Ticket ticket);

    @StaffReceiver
    @Message("announce.update")
    void announceUpdate(@Causer @Placeholder Soul causer, @Placeholder Ticket ticket);

    @StaffReceiver
    @Message("announce.close")
    void announceClose(@Causer @Placeholder Soul causer, @Placeholder Ticket ticket);

    @StaffReceiver
    @Message("announce.reopen")
    void announceReopen(@Causer @Placeholder Soul causer, @Placeholder Ticket ticket);

    @StaffReceiver
    @Message("announce.claim")
    void announceClaim(@Causer @Placeholder Soul causer, @Placeholder Ticket ticket);

    @StaffReceiver
    @Message("announce.unclaim")
    void announceUnclaim(@Causer @Placeholder Soul causer, @Placeholder Ticket ticket);

    @StaffReceiver
    @Message("announce.assign")
    void announceAssign(@Causer @Placeholder Soul causer, @Causer @Placeholder Soul target, @Placeholder Ticket ticket);

    @StaffReceiver
    @Message("announce.complete")
    void announceComplete(@Causer @Placeholder Soul causer, @Placeholder Ticket ticket);

    @StaffReceiver
    @Message("announce.note")
    void announceNote(@Causer @Placeholder Soul causer, @Placeholder Ticket ticket, @Placeholder String note);


}
