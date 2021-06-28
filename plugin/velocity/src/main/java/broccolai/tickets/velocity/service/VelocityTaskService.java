package broccolai.tickets.velocity.service;

import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class VelocityTaskService implements TaskService {

    private final PluginPlatform platform;
    private final ProxyServer server;

    @Inject
    public VelocityTaskService(final @NonNull PluginPlatform platform, final @NonNull ProxyServer server) {
        this.platform = platform;
        this.server = server;
    }

    @Override
    public void sync(final @NonNull Runnable runnable) {
        this.server.getScheduler().buildTask(this.platform, runnable).schedule();
    }

    @Override
    public void async(final @NonNull Runnable runnable) {
        this.server.getScheduler().buildTask(this.platform, runnable).schedule();
    }

    @Override
    public void schedule(final @NonNull Task task) {
        this.server.getScheduler().buildTask(this.platform, task)
                .delay(task.delay() / 20, TimeUnit.SECONDS)
                .repeat(task.repeat() / 20, TimeUnit.SECONDS)
                .schedule();
    }

    @Override
    public void clear() {
    }

}
