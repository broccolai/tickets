package broccolai.tickets.bukkit.service;

import broccolai.tickets.api.model.task.RepeatTask;
import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.service.tasks.TaskService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class BukkitTaskService implements TaskService {

    private final Plugin plugin;

    @Inject
    public BukkitTaskService(final @NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sync(final @NonNull Runnable runnable) {
        Bukkit.getScheduler().runTask(this.plugin, runnable);
    }

    @Override
    public void async(final @NonNull Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

    @Override
    public void schedule(final @NonNull Task task) {
        if (task instanceof RepeatTask) {
            RepeatTask repeatTask = (RepeatTask) task;
            Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, task, task.delay(), repeatTask.repeat());
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, task, task.delay());
        }
    }

    @Override
    public void clear() {
        Bukkit.getScheduler().cancelTasks(this.plugin);
    }

}
