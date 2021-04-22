package broccolai.tickets.core.tasks;

import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.core.configuration.MainConfiguration;
import broccolai.tickets.core.configuration.TasksConfiguration;
import broccolai.tickets.core.utilities.Constants;
import com.google.inject.Inject;
import java.util.EnumSet;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ReminderTask implements Task {

    private final TasksConfiguration.ReminderTaskConfiguration config;
    private final UserService userService;
    private final TicketService ticketService;
    private final MessageService messageService;

    @Inject
    public ReminderTask(
            final @NonNull MainConfiguration mainConfiguration,
            final @NonNull UserService userService,
            final @NonNull TicketService ticketService,
            final @NonNull MessageService messageService
    ) {
        this.config = mainConfiguration.tasksConfiguration.reminder;
        this.userService = userService;
        this.ticketService = ticketService;
        this.messageService = messageService;
    }

    @Override
    public void run() {
        int tickets = this.ticketService.get(EnumSet.of(TicketStatus.OPEN, TicketStatus.CLAIMED)).size();

        if (tickets == 0) {
            return;
        }

        Component component = this.messageService.taskReminder(tickets);

        for (PlayerSoul soul : this.userService.players()) {
            if (!soul.permission(Constants.STAFF_PERMISSION + ".remind")) {
                continue;
            }

            soul.sendMessage(component);
        }
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
