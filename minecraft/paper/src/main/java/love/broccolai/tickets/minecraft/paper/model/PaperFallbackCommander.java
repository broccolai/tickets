package love.broccolai.tickets.minecraft.paper.model;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import love.broccolai.tickets.minecraft.common.model.Commander;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import java.util.UUID;

@NullMarked
public record PaperFallbackCommander(
    CommandSender commandSender,
    CommandSourceStack commandSourceStack
) implements Commander {

    @Override
    public Audience audience() {
        return this.commandSender;
    }

    @Override
    public UUID uuid() {
        return this.commandSender.get(Identity.UUID).orElseThrow();
    }
}
