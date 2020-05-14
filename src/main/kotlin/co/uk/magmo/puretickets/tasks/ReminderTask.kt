package co.uk.magmo.puretickets.tasks

import co.uk.magmo.puretickets.interactions.Notifications
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.ticket.TicketManager
import co.uk.magmo.puretickets.ticket.TicketStatus
import co.uk.magmo.puretickets.utils.Constants
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class ReminderTask : BukkitRunnable() {
    override fun run() {
        val amount = TicketManager.asMap().values.flatten()
                .count { it.status == TicketStatus.OPEN }

        if (amount == 0) return

        Bukkit.getOnlinePlayers()
                .filter { it.hasPermission(Constants.STAFF_PERMISSION + ".remind") }
                .forEach { Notifications.reply(it, Messages.TASKS__REMINDER, "%amount%", amount.toString()) }
    }
}