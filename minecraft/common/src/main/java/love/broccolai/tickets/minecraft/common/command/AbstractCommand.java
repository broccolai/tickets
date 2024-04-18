package love.broccolai.tickets.minecraft.common.command;

import love.broccolai.tickets.minecraft.common.model.Commander;
import org.incendo.cloud.CommandManager;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractCommand {

    public abstract void register(final CommandManager<Commander> commandManager);
}
