package love.broccolai.tickets.minecraft.paper.model;

import java.util.UUID;
import love.broccolai.tickets.minecraft.common.model.Commander;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperConsoleCommander(
    ConsoleCommandSender consoleCommandSender
) implements Commander {

    private static final UUID CONSOLE_FAKE_UUID = new UUID(0L, 0L);

    public static Commander of(CommandSender commandSender) {
        if (commandSender instanceof Player player) {
            return new PaperPlayerCommander(player);
        }

        if (commandSender instanceof ConsoleCommandSender consoleCommandSender) {
            return new PaperConsoleCommander(consoleCommandSender);
        }

        throw new IllegalArgumentException();
    }

    public static CommandSender sender(Commander commander) {
        if (commander instanceof PaperPlayerCommander playerCommander) {
            return playerCommander.player();
        }

        if (commander instanceof PaperConsoleCommander paperConsoleCommander) {
            return paperConsoleCommander.consoleCommandSender();
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
