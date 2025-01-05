package love.broccolai.tickets.minecraft.paper.model;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.SenderMapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperSenderMapper implements SenderMapper<CommandSourceStack, Commander> {
    @Override
    public Commander map(final CommandSourceStack base) {
        CommandSender commandSender = base.getSender();

        if (commandSender instanceof Player player) {
            return new PaperPlayerCommander(player, base);
        }

        if (commandSender instanceof ConsoleCommandSender consoleCommandSender) {
            return new PaperConsoleCommander(consoleCommandSender, base);
        }

        return new PaperFallbackCommander(commandSender, base);
    }

    @Override
    public CommandSourceStack reverse(final Commander mapped) {
        return switch (mapped) {
            case PaperPlayerCommander playerCommander -> playerCommander.commandSourceStack();
            case PaperConsoleCommander paperConsoleCommander -> paperConsoleCommander.commandSourceStack();
            case PaperFallbackCommander fallbackCommander -> fallbackCommander.commandSourceStack();
            default -> throw new IllegalArgumentException();
        };
    }
}
