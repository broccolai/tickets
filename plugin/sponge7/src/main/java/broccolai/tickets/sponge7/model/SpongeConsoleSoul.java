package broccolai.tickets.sponge7.model;

import broccolai.tickets.api.model.user.ConsoleSoul;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;

public final class SpongeConsoleSoul extends ConsoleSoul implements SpongeOnlineSoul {

    public SpongeConsoleSoul(final @NonNull Audience audience) {
        super(audience);
    }

    @Override
    public CommandSource sender() {
        return Sponge.getServer().getConsole();
    }

}
