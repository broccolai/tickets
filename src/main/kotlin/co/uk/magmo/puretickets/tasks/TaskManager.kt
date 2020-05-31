package co.uk.magmo.puretickets.tasks

import com.okkero.skedule.BukkitSchedulerController
import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class TaskManager(private val plugin: Plugin) {
    private val scheduler = Bukkit.getScheduler()
    private val tasks = ArrayList<BukkitTask>()

    operator fun invoke(initialContext: SynchronizationContext = SynchronizationContext.ASYNC, co: suspend BukkitSchedulerController.() -> Unit) = scheduler.schedule(plugin, initialContext, co)

    fun addRepeatingTask(task: BukkitRunnable, delay: Long, repeat: Long) = tasks.add(task.runTaskTimerAsynchronously(plugin, delay, repeat))

    fun clear() = tasks.forEach { it.cancel() }
}