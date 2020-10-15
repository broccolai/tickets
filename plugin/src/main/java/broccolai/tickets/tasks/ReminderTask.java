package broccolai.tickets.tasks;

import broccolai.tickets.locale.Messages;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.user.UserManager;
import broccolai.tickets.utilities.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Task for displaying reminders.
 */
public final class ReminderTask extends BukkitRunnable {

    @NonNull
    private final UserManager userManager;

    /**
     * Initialise a new Reminder Task.
     *
     * @param userManager the user manager instance
     */
    public ReminderTask(@NonNull final UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void run() {
        int amount = TicketSQL.count(TicketStatus.OPEN);

        if (amount == 0) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(Constants.STAFF_PERMISSION + ".remind")) {
                userManager.fromPlayer(player).message(Messages.OTHER__REMINDER, "amount", String.valueOf(amount));
            }
        }
    }

}
