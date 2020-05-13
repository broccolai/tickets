package co.uk.magmo.puretickets.tasks

import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS
import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.utils.minuteToTick
import org.bukkit.scheduler.BukkitTask

class TaskManager {
    private val tasks = ArrayList<BukkitTask>()

    init {
        tasks += ReminderTask().runTaskTimerAsynchronously(TICKETS, Config.reminderDelay.minuteToTick(), Config.reminderRepeat.minuteToTick())
    }

    fun clear() = tasks.forEach { it.cancel() }
}