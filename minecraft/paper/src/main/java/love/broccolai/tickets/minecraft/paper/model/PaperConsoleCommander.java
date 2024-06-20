package love.broccolai.tickets.minecraft.paper.model;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.UUID;
import love.broccolai.tickets.minecraft.common.model.Commander;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperConsoleCommander(
    ConsoleCommandSender consoleCommandSender,
    CommandSourceStack commandSourceStack
) implements Commander {

    private static final UUID CONSOLE_FAKE_UUID = new UUID(0L, 0L);

    public static Commander of(CommandSourceStack sourceStack) {
        CommandSender commandSender = sourceStack.getSender();

        if (commandSender instanceof Player player) {
            return new PaperPlayerCommander(player, sourceStack);
        }

        if (commandSender instanceof ConsoleCommandSender consoleCommandSender) {
            return new PaperConsoleCommander(consoleCommandSender, sourceStack);
        }

        throw new IllegalArgumentException();
    }

    public static CommandSourceStack sender(Commander commander) {
        if (commander instanceof PaperPlayerCommander playerCommander) {
            return playerCommander.commandSourceStack();
        }

        if (commander instanceof PaperConsoleCommander paperConsoleCommander) {
            return paperConsoleCommander.commandSourceStack();
        }

        throw new IllegalArgumentException();
    }

    @Override
    public Audience audience() {
        return this.consoleCommandSender();
    }

    @Override
    public UUID uuid() {
        return CONSOLE_FAKE_UUID;
    }
}
