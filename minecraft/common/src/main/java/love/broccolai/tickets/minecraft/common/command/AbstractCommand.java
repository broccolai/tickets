package love.broccolai.tickets.minecraft.common.command;

import cloud.commandframework.CommandManager;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractCommand {

    public abstract void register(final CommandManager<Commander> commandManager);
}
