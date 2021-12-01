package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.user.OnlineUser;
import cloud.commandframework.CommandManager;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface BaseCommand {

    void register(@NonNull CommandManager<OnlineUser> manager);

}
