package broccolai.tickets.sponge7.model;

import broccolai.tickets.api.model.user.OnlineSoul;
import org.spongepowered.api.command.CommandSource;

public interface SpongeOnlineSoul extends OnlineSoul {

    CommandSource sender();

}
