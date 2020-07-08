package co.uk.magmo.puretickets.tasks;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskManager {
    private final Plugin plugin;
    private final TaskChainFactory taskChainFactory;
    private final List<BukkitTask> tasks = new ArrayList<>();

    public TaskManager(Plugin plugin) {
        this.plugin = plugin;
        taskChainFactory = BukkitTaskChainFactory.create(plugin);
    }

    public <T> TaskChain<T> use() {
        return taskChainFactory.newChain();
    }

    public void addRepeatingTask(BukkitRunnable runnable, Long delay, Long repeat) {
        BukkitTask task = runnable.runTaskTimerAsynchronously(plugin, delay, repeat);
        tasks.add(task);
    }

    public void clear() {
        Iterator<BukkitTask> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            BukkitTask task = iterator.next();

            task.cancel();
            iterator.remove();
        }
    }
}