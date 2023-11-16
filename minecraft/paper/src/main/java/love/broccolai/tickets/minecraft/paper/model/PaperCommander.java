package love.broccolai.tickets.minecraft.paper.model;

import love.broccolai.tickets.minecraft.common.model.Commander;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperCommander(
    CommandSender commandSender
) implements Commander {

    public static Commander of(CommandSender commandSender) {
        if (commandSender instanceof Player player) {
            return new PaperPlayerCommander(player);
        }

        return new PaperCommander(commandSender);
    }

    public static CommandSender sender(Commander commander) {
        if (commander instanceof PaperPlayerCommander playerCommander) {
            return playerCommander.player();
        }

        if (commander instanceof PaperCommander paperCommander) {
            return paperCommander.commandSender();
        }

        throw new IllegalArgumentException();
    }

    @Override
    public Audience audience() {
        return this.commandSender;
    }
}
