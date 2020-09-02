package broccolai.tickets.tasks;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Manager for creating a clearing tasks.
 */
public class TaskManager {
    private final Plugin plugin;
    private final TaskChainFactory taskChainFactory;
    private final List<BukkitTask> tasks = new ArrayList<>();

    /**
     * Initialise a new Task Manager.
     * @param plugin the plugin instance to register against.
     */
    public TaskManager(Plugin plugin) {
        this.plugin = plugin;
        taskChainFactory = BukkitTaskChainFactory.create(plugin);
    }

    /**
     * Create a new TaskChain instance and return it.
     * @param <T> chain type
     * @return a new task chain instance
     */
    public <T> TaskChain<T> use() {
        return taskChainFactory.newChain();
    }

    /**
     * Add a new repeating task to the manager.
     * @param runnable the runnable to repeat
     * @param delay the time before the first task run
     * @param repeat the time in between each task run
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