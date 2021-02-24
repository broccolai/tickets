package broccolai.tickets.bukkit.tasks;

import broccolai.tickets.core.tasks.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BukkitTaskManager implements TaskManager {

    private final Plugin plugin;
    private final List<BukkitTask> tasks = new ArrayList<>();

    /**
     * Create a new BukkitTaskManager
     *
     * @param plugin Plugin instance to reference
     */
    public BukkitTaskManager(final @NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void async(final @NonNull Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public void addRepeatingTask(final @NonNull Runnable runnable, final long delay, final long repeat) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, repeat);
        this.tasks.add(task);
    }

    @Override
    public void clear() {
        Iterator<BukkitTask> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            BukkitTask task = iterator.next();

            task.cancel();
            iterator.remove();
        }
    }

}
