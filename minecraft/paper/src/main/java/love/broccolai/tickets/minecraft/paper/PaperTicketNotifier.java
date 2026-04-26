package love.broccolai.tickets.minecraft.paper;

import com.google.inject.Inject;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.AssociatedTicketAction;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.model.action.packaged.TicketAssigned;
import love.broccolai.tickets.api.model.action.packaged.TicketClosed;
import love.broccolai.tickets.api.model.action.packaged.TicketCommented;
import love.broccolai.tickets.api.model.action.packaged.TicketOpened;
import love.broccolai.tickets.api.model.action.packaged.TicketReopened;
import love.broccolai.tickets.api.model.action.packaged.TicketUnassigned;
import love.broccolai.tickets.minecraft.common.service.MessageService;
import love.broccolai.tickets.minecraft.common.service.TicketNotifier;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperTicketNotifier implements TicketNotifier {

    private final MessageService messages;

    @Inject
    public PaperTicketNotifier(final MessageService messages) {
        this.messages = messages;
    }

    @Override
    public void notify(final AssociatedTicketAction associatedAction, final Ticket ticket) {
        this.notify(associatedAction.action(), ticket);
    }

    private void notify(final TicketAction action, final Ticket ticket) {
        if (action instanceof TicketOpened) {
            this.messages.notificationStaffCreated(action.creator(), ticket);
        } else if (action instanceof TicketCommented) {
            this.messages.notificationStaffCommented(action.creator(), ticket);
        } else if (action instanceof TicketAssigned assigned && assigned.creator().equals(assigned.assignee())) {
            this.messages.notificationStaffClaimed(action.creator(), ticket);
        } else if (action instanceof TicketAssigned) {
            this.messages.notificationStaffAssigned(action.creator(), ticket);
        } else if (action instanceof TicketUnassigned) {
            this.messages.notificationStaffUnclaimed(action.creator(), ticket);
        } else if (action instanceof TicketClosed) {
            this.messages.notificationStaffClosed(action.creator(), ticket);
        } else if (action instanceof TicketReopened) {
            this.messages.notificationStaffReopened(action.creator(), ticket);
        }
    }
}
