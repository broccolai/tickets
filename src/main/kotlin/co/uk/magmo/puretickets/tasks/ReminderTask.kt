package co.uk.magmo.puretickets.tasks

import co.uk.magmo.puretickets.interactions.NotificationManager
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.ticket.TicketManager
import co.uk.magmo.puretickets.ticket.TicketStatus
import co.uk.magmo.puretickets.utils.Constants
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class ReminderTask(private val ticketManager: TicketManager, private val notificationManager: NotificationManager) : BukkitRunnable() {
    override fun run() {
        val amount = ticketManager.asMap().values.flatten()
                .count { it.status == TicketStatus.OPEN }

        if (amount == 0) return

        Bukkit.getOnlinePlayers()
                .filter { it.hasPermission(Constants.STAFF_PERMISSION + ".remind") }
                .forEach { notificationManager.reply(it, Messages.TASKS__REMINDER, "%amount%", amount.toString()) }
    }
}