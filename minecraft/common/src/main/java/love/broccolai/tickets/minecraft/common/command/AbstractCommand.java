package love.broccolai.tickets.minecraft.common.command;

import cloud.commandframework.Command;
import cloud.commandframework.Command.Builder;
import cloud.commandframework.CommandFactory;
import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandProperties;
import cloud.commandframework.meta.CommandMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractCommand implements CommandFactory<Commander> {

    @Override
    public List<Command<Commander>> createCommand(final CommandManager<Commander> commandManager) {
        Builder<Commander> builder = commandManager.commandBuilder(
            this.baseProperties().name(),
            this.baseProperties().aliases(),
            CommandMeta.simple().build()
        );

        List<Command<Commander>> commands = new ArrayList<>();

        this.register(creator -> {
            Builder<Commander> result = creator.apply(builder);
            commands.add(result.build());
        });

        return commands;
    }

    public abstract CommandProperties baseProperties();

    public abstract void register(CommandRegister commandRegister);

    @FunctionalInterface
    public interface CommandRegister {
        void create(UnaryOperator<Builder<Commander>> operator);
    }
}
