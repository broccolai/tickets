package broccolai.tickets.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class TaskManager {

    private final Plugin plugin;
    private final List<BukkitTask> tasks = new ArrayList<>();

    /**
     * Initialise a new Task Manager
     *
     * @param plugin Plugin instance
     */
    public TaskManager(final @NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Run something async
     *
     * @param runnable Runnable
     */
    public void async(final @NonNull Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    /**
     * Add a new repeating task to the manager
     *
     * @param runnable Runnable
     * @param delay    Delay long
     * @param repeat   Repeat long
     */
    public void addRepeatingTask(final @NonNull BukkitRunnable runnable, final long delay, final long repeat) {
        BukkitTask task = runnable.runTaskTimerAsynchronously(plugin, delay, repeat);
        tasks.add(task);
    }

    /**
     * Clear the current tasks from the manager
     */
    public void clear() {
        Iterator<BukkitTask> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            BukkitTask task = iterator.next();

            task.cancel();
            iterator.remove();
        }
    }

}
