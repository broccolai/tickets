package broccolai.tickets.core.commands.command;

import broccolai.tickets.core.model.user.UserAudience;
import cloud.commandframework.CommandManager;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface BaseCommand {

    void register(@NonNull CommandManager<UserAudience> manager);

}
