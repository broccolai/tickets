package broccolai.tickets.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/**
 * Manager for creating a clearing tasks.
 */
public class TaskManager {
    private final Plugin plugin;
    private final List<BukkitTask> tasks = new ArrayList<>();

    /**
     * Initialise a new Task Manager.
     *
     * @param plugin the plugin instance to register against.
     */
    public TaskManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void async(@NotNull final Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    /**
     * Add a new repeating task to the manager.
     *
     * @param runnable the runnable to repeat
     * @param delay    the time before the first task run
     * @param repeat   the time in between each task run
     */
    public void addRepeatingTask(BukkitRunnable runnable, long delay, long repeat) {
        BukkitTask task = runnable.runTaskTimerAsynchronously(plugin, delay, repeat);
        tasks.add(task);
    }

    /**
     * Clear the current tasks from the manager.
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