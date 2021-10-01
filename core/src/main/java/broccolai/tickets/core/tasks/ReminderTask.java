package broccolai.tickets.core.tasks;

import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.core.configuration.MainConfiguration;
import broccolai.tickets.core.configuration.TasksConfiguration;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import java.util.EnumSet;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ReminderTask implements Task {

    private final TasksConfiguration.ReminderTaskConfiguration config;
    private final TicketService ticketService;
    private final MessageService messageService;

    @Inject
    public ReminderTask(
            final @NonNull MainConfiguration mainConfiguration,
            final @NonNull TicketService ticketService,
            final @NonNull MessageService messageService
    ) {
        this.config = mainConfiguration.tasksConfiguration.reminder;
        this.ticketService = ticketService;
        this.messageService = messageService;
    }

    @Override
    public void run() {
        Multimap<UUID, Ticket> tickets = this.ticketService.get(EnumSet.of(TicketStatus.OPEN, TicketStatus.CLAIMED));
        int total = tickets.entries().size();

        if (total == 0) {
            return;
        }

        this.messageService.taskReminder(total);
    }

    @Override
    public long delay() {
        return (long) this.config.delay * 60 * 20;
    }

    @Override
    public long repeat() {
        return (long) this.config.repeat * 60 * 20;
    }

}
