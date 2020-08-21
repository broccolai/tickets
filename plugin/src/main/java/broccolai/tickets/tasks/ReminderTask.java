package broccolai.tickets.tasks;

import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.utilities.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ReminderTask extends BukkitRunnable {
    private final TicketManager ticketManager;
    private final NotificationManager notificationManager;

    public ReminderTask(TicketManager ticketManager, NotificationManager notificationManager) {
        this.ticketManager = ticketManager;
        this.notificationManager = notificationManager;
    }

    @Override
    public void run() {
        Integer amount = ticketManager.count(TicketStatus.OPEN);

        if (amount == 0) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(Constants.STAFF_PERMISSION + ".remind")) {
                notificationManager.basic(player, Messages.OTHER__REMINDER, "%amount%", amount.toString());
            }
        }
    }
}