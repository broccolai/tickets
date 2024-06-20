package love.broccolai.tickets.minecraft.paper.model;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.UUID;
import love.broccolai.tickets.minecraft.common.model.PlayerCommander;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperPlayerCommander(
    Player player,
    CommandSourceStack commandSourceStack
) implements PlayerCommander {

    @Override
    public Audience audience() {
        return this.player;
    }

    @Override
    public UUID uuid() {
        return this.player.getUniqueId();
    }
}
