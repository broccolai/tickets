package co.uk.magmo.puretickets.tasks;

import co.uk.magmo.puretickets.interactions.NotificationManager;
import co.uk.magmo.puretickets.locale.Messages;
import co.uk.magmo.puretickets.ticket.TicketManager;
import co.uk.magmo.puretickets.ticket.TicketStatus;
import co.uk.magmo.puretickets.utilities.Constants;
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

        if (amount == 0) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(Constants.STAFF_PERMISSION + ".remind")) {
                notificationManager.basic(player, Messages.OTHER__REMINDER, "%amount%", amount.toString());
            }
        }
    }
}