package broccolai.tickets.core.subscribers;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.event.impl.SoulJoinEvent;
import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.core.configuration.MainConfiguration;
import broccolai.tickets.core.configuration.TasksConfiguration;
import broccolai.tickets.core.utilities.Constants;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.EnumSet;

public final class SoulSubscriber implements Subscriber {

    private final StorageService storageService;
    private final TicketService ticketService;
    private final MessageService messageService;
    private final TaskService taskService;
    private final TasksConfiguration tasksConfiguration;

    @Inject
    public SoulSubscriber(
            final @NonNull StorageService storageService,
            final @NonNull TicketService ticketService,
            final @NonNull MessageService messageService,
            final @NonNull TaskService taskService,
            final @NonNull MainConfiguration mainConfiguration
    ) {
        this.storageService = storageService;
        this.ticketService = ticketService;
        this.messageService = messageService;
        this.taskService = taskService;
        this.tasksConfiguration = mainConfiguration.tasksConfiguration;
    }

    @Override
    public void register(final @NonNull EventService eventService) {
        eventService.register(SoulJoinEvent.class, this::onSoulJoin);
    }

    public void onSoulJoin(final @NonNull SoulJoinEvent event) {
        PlayerSoul soul = event.soul();
        Task task = new LoginReminderTask(soul);

        this.taskService.schedule(task);
    }

    private final class LoginReminderTask implements Task {

        private final PlayerSoul soul;

        private LoginReminderTask(final @NonNull PlayerSoul soul) {
            this.soul = soul;
        }

        @Override
        public void run() {
            int tickets = SoulSubscriber.this.ticketService.get(EnumSet.of(TicketStatus.OPEN, TicketStatus.CLAIMED)).size();

            if (this.soul.permission(Constants.STAFF_PERMISSION + ".announce") && tickets != 0) {
                this.soul.sendMessage(SoulSubscriber.this.messageService.taskReminder(tickets));
            }

            Collection<Component> notifications = SoulSubscriber.this.storageService.notifications(this.soul);

            for (final Component notification : notifications) {
                this.soul.sendMessage(notification);
            }
        }

        @Override
        public long delay() {
            return (long) SoulSubscriber.this.tasksConfiguration.joinReminderDelay * 60 * 20;
        }

    }

}
