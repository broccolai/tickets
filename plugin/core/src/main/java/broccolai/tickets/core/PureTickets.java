package broccolai.tickets.core;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.core.commands.command.BaseCommand;
import broccolai.tickets.core.inject.module.ConfigurationModule;
import broccolai.tickets.core.inject.module.FactoryModule;
import broccolai.tickets.core.inject.module.ServiceModule;
import broccolai.tickets.core.service.user.snapshot.CacheSnapshotService;
import broccolai.tickets.core.tasks.ReminderTask;
import broccolai.tickets.core.tasks.SaveTask;
import broccolai.tickets.core.utilities.ArrayHelper;
import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PureTickets {

    private final Class<? extends Task>[] tasks = ArrayHelper.create(
            ReminderTask.class,
            SaveTask.class
    );

    private final Injector parentInjector;
    private @MonotonicNonNull Injector injector;

    @Inject
    public PureTickets(final @NonNull Injector parentInjector) {
        this.parentInjector = parentInjector;
    }

    public Injector load() {
        this.injector = this.parentInjector.createChildInjector(
                new ConfigurationModule(),
                new ServiceModule(),
                new FactoryModule()
        );

        TaskService taskService = this.injector.getInstance(TaskService.class);

        for (final Class<? extends Task> task : this.tasks) {
            taskService.schedule(this.injector.getInstance(task));
        }

        return this.injector;
    }

    public void unload() {
        this.injector.getInstance(StorageService.class).dispose();
        this.injector.getInstance(CacheSnapshotService.class).dispose();
    }

    public void subscribers(final @NonNull Class<? extends Subscriber>[] subscribers) {
        EventService eventService = this.injector.getInstance(EventService.class);

        for (final Class<? extends Subscriber> subscriber : subscribers) {
            this.injector.getInstance(subscriber).register(eventService);
        }
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
