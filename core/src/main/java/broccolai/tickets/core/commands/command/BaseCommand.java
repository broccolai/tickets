package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.user.OnlineSoul;
import cloud.commandframework.CommandManager;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface BaseCommand {

    void register(@NonNull CommandManager<OnlineSoul> manager);

}
