package broccolai.tickets.bukkit.model.User;

import broccolai.tickets.core.model.user.OnlineSoul;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface BukkitOnlineSoul extends OnlineSoul {
    @NonNull CommandSender sender();
}
