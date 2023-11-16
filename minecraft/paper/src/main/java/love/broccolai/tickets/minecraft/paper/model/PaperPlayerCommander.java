package love.broccolai.tickets.minecraft.paper.model;

import love.broccolai.tickets.minecraft.common.model.PlayerCommander;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperPlayerCommander(
    Player player
) implements PlayerCommander {

    @Override
    public Audience audience() {
        return this.player;
    }
}
