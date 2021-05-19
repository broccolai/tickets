package broccolai.tickets.core;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.core.commands.cloud.CloudSuggestionProcessor;
import broccolai.tickets.core.commands.cloud.ExceptionHandler;
import broccolai.tickets.core.commands.command.BaseCommand;
import broccolai.tickets.core.service.user.snapshot.CacheSnapshotService;
import broccolai.tickets.core.subscribers.NotificationSubscriber;
import broccolai.tickets.core.subscribers.SoulSubscriber;
import broccolai.tickets.core.tasks.ReminderTask;
import broccolai.tickets.core.tasks.SaveTask;
import broccolai.tickets.core.utilities.ArrayHelper;
import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PureTickets {

    private static final Class<? extends Task>[] DEFAULT_TASKS = ArrayHelper.create(
            ReminderTask.class,
            SaveTask.class
    );

    private static final Class<? extends Subscriber>[] DEFAULT_SUBSCRIBERS = ArrayHelper.create(
            NotificationSubscriber.class,
            SoulSubscriber.class
    );

    private final Injector injector;
    private final TaskService taskService;
    private final EventService eventService;

    @Inject
    public PureTickets(
            final @NonNull Injector injector,
            final @NonNull TaskService taskService,
            final @NonNull EventService eventService
    ) {
        this.injector = injector;
        this.taskService = taskService;
        this.eventService = eventService;
    }

    public void load() {
        this.tasks(DEFAULT_TASKS);
        this.subscribers(DEFAULT_SUBSCRIBERS);
    }

    public void unload() {
        this.injector.getInstance(StorageService.class).dispose();
        this.injector.getInstance(CacheSnapshotService.class).dispose();
    }

    public void tasks(final @NonNull Class<? extends Task>[] tasks) {
        for (final Class<? extends Task> task : tasks) {
            this.taskService.schedule(this.injector.getInstance(task));
        }
    }

    public void subscribers(final @NonNull Class<? extends Subscriber>[] subscribers) {
        for (final Class<? extends Subscriber> subscriber : subscribers) {
            this.injector.getInstance(subscriber).register(this.eventService);
        }
    }

    public void defaultCommandManagerSettings(final @NonNull CommandManager<OnlineSoul> commandManager) {
        ExceptionHandler exceptionHandler = this.injector.getInstance(ExceptionHandler.class);
        CloudSuggestionProcessor suggestionProcessor = this.injector.getInstance(CloudSuggestionProcessor.class);

        exceptionHandler.apply(commandManager);
        commandManager.setCommandSuggestionProcessor(suggestionProcessor);
    }

    public void commands(
            final @NonNull CommandManager<OnlineSoul> commandManager,
            final @NonNull Class<? extends BaseCommand>[] commands
    ) {
        for (final Class<? extends BaseCommand> commandClazz : commands) {
            BaseCommand command = this.injector.getInstance(commandClazz);
            command.register(commandManager);
        }
    }

}
