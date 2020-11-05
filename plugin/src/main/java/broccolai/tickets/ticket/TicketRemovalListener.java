package broccolai.tickets.ticket;

import broccolai.tickets.tasks.TaskManager;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.checkerframework.checker.nullness.qual.NonNull;

final class TicketRemovalListener implements RemovalListener<Integer, Ticket> {

    private final TaskManager taskManager;
    private final TicketManager ticketManager;

    TicketRemovalListener(final @NonNull TaskManager taskManager, final @NonNull TicketManager ticketManager) {
        this.taskManager = taskManager;
        this.ticketManager = ticketManager;
    }

    @Override
    public void onRemoval(final RemovalNotification<Integer, Ticket> notification) {
        Ticket ticket = notification.getValue();

        if (ticket.isDirty()) {
            taskManager.async(() -> ticketManager.updateTicket(ticket));
        }
    }

}
