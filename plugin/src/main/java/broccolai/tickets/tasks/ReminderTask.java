package broccolai.tickets.tasks;

import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.utilities.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Task for displaying reminders.
 */
public class ReminderTask extends BukkitRunnable {
    @NotNull
    private final NotificationManager notificationManager;

    /**
     * Initialise a new Reminder Task.
     *
     * @param notificationManager the notification manager instance
     */
    public ReminderTask(@NotNull NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @Override
    public void run() {
        int amount = TicketSQL.count(TicketStatus.OPEN);

        if (amount == 0) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(Constants.STAFF_PERMISSION + ".remind")) {
                notificationManager.basic(player, Messages.OTHER__REMINDER, "%amount%", String.valueOf(amount));
            }
        }
    }
}