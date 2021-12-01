package broccolai.tickets.velocity.model;

import broccolai.tickets.api.model.user.OnlineUser;
import com.velocitypowered.api.command.CommandSource;

public interface VelocityOnlineUser extends OnlineUser {

    CommandSource source();

}
