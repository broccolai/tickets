package broccolai.tickets.paper.model;

import broccolai.tickets.api.model.user.ConsoleSoul;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PaperConsoleSoul extends ConsoleSoul implements PaperOnlineSoul {

    public PaperConsoleSoul(final @NonNull Audience audience) {
        super(audience);
    }

    @Override
    public CommandSender sender() {
        return Bukkit.getConsoleSender();
    }

}
