package broccolai.tickets.bukkit.model;

import broccolai.tickets.api.model.user.OnlineUser;
import org.bukkit.command.CommandSender;

public interface BukkitOnlineUser extends OnlineUser {

    CommandSender sender();

}
