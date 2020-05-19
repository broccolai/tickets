package co.uk.magmo.puretickets.tasks

import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.interactions.NotificationManager
import co.uk.magmo.puretickets.ticket.TicketManager
import co.uk.magmo.puretickets.utils.minuteToTick
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

class TaskManager(plugin: Plugin, ticketManager: TicketManager, notificationManager: NotificationManager) {
    private val tasks = ArrayList<BukkitTask>()

    init {
        tasks += ReminderTask(ticketManager, notificationManager)
                .runTaskTimerAsynchronously(plugin, Config.reminderDelay.minuteToTick(), Config.reminderRepeat.minuteToTick())
    }

    fun clear() = tasks.forEach { it.cancel() }
}