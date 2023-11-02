package broccolai.tickets.paper.model;

import broccolai.tickets.api.model.user.OnlineSoul;
import org.bukkit.command.CommandSender;

public interface PaperOnlineSoul extends OnlineSoul {

    CommandSender sender();

}
