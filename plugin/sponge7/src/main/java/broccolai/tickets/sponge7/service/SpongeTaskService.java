package broccolai.tickets.sponge7.service;

import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public final class SpongeTaskService implements TaskService {

    private final PluginPlatform plugin;
    private final SpongeExecutorService syncExecutor;
    private final SpongeExecutorService asyncExecutor;

    @Inject
    public SpongeTaskService(final @NonNull PluginPlatform plugin) {
        this.plugin = plugin;
        this.syncExecutor = Sponge.getScheduler().createSyncExecutor(plugin);
        this.asyncExecutor = Sponge.getScheduler().createAsyncExecutor(plugin);
    }

    @Override
    public void sync(@NonNull final Runnable runnable) {
        this.syncExecutor.execute(runnable);
    }

    @Override
    public void async(final @NonNull Runnable runnable) {
        this.asyncExecutor.execute(runnable);
    }

    @Override
    public void schedule(final @NonNull Task task) {
        this.asyncExecutor.scheduleWithFixedDelay(task, task.delay() / 20, task.repeat() / 20, TimeUnit.SECONDS);
    }

    @Override
    public void clear() {
        Sponge.getScheduler().getScheduledTasks(this.plugin).forEach(org.spongepowered.api.scheduler.Task::cancel);
    }

}
