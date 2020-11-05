package broccolai.tickets.tasks;

import broccolai.tickets.locale.Messages;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.user.UserManager;
import broccolai.tickets.utilities.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Task to remind online staff members of current tickets
 */
public final class ReminderTask extends BukkitRunnable {

    private final UserManager userManager;
    private final TicketManager ticketManager;

    /**
     * Create a reminder task
     *
     * @param userManager   User manager
     * @param ticketManager Ticket manager
     */
    public ReminderTask(@NonNull final UserManager userManager, final @NonNull TicketManager ticketManager) {
        this.userManager = userManager;
        this.ticketManager = ticketManager;
    }

    @Override
    public void run() {
        int amount = ticketManager.countTickets(TicketStatus.OPEN);

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
