package broccolai.tickets.sponge.model;

import broccolai.tickets.api.model.user.OnlineSoul;
import org.spongepowered.api.command.CommandSource;

public interface SpongeOnlineSoul extends OnlineSoul {

    CommandSource sender();

}
