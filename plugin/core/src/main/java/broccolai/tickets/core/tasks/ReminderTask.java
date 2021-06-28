package broccolai.tickets.core.tasks;

import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.message.OldMessageService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.core.configuration.MainConfiguration;
import broccolai.tickets.core.configuration.TasksConfiguration;
import broccolai.tickets.core.utilities.Constants;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import java.util.EnumSet;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ReminderTask implements Task {

    private final TasksConfiguration.ReminderTaskConfiguration config;
    private final UserService userService;
    private final TicketService ticketService;
    private final OldMessageService oldMessageService;

    @Inject
    public ReminderTask(
            final @NonNull MainConfiguration mainConfiguration,
            final @NonNull UserService userService,
            final @NonNull TicketService ticketService,
            final @NonNull OldMessageService oldMessageService
    ) {
        this.config = mainConfiguration.tasksConfiguration.reminder;
        this.userService = userService;
        this.ticketService = ticketService;
        this.oldMessageService = oldMessageService;
    }

    @Override
    public void run() {
        Multimap<UUID, Ticket> tickets = this.ticketService.get(EnumSet.of(TicketStatus.OPEN, TicketStatus.CLAIMED));
        int total = tickets.entries().size();

        if (total == 0) {
            return;
        }

        Component component = this.oldMessageService.taskReminder(total);

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
