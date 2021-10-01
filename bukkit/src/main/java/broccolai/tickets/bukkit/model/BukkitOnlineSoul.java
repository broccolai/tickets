package broccolai.tickets.bukkit.model;

import broccolai.tickets.api.model.user.OnlineSoul;
import org.bukkit.command.CommandSender;

public interface BukkitOnlineSoul extends OnlineSoul {

    CommandSender sender();

}
