package broccolai.tickets.velocity.model;

import broccolai.tickets.api.model.user.OnlineSoul;
import com.velocitypowered.api.command.CommandSource;

public interface VelocityOnlineSoul extends OnlineSoul {

    CommandSource source();

}
