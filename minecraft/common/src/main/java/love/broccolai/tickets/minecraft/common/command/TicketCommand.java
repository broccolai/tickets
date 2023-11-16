package love.broccolai.tickets.minecraft.common.command;

import cloud.commandframework.CommandProperties;
import cloud.commandframework.arguments.standard.StringArgument;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketCommand extends AbstractCommand {

    @Override
    public CommandProperties baseProperties() {
        return CommandProperties.commandProperties("ticket", "ti");
    }

    @Override
    public void register(CommandRegister commandRegister) {
        commandRegister.create(base -> base
            .literal("create")
            .required(StringArgument.greedy("message"))
        );
    }
}
