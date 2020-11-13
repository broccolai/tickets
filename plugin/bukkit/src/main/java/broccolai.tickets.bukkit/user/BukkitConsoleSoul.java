package broccolai.tickets.bukkit.user;

import broccolai.tickets.core.user.ConsoleSoul;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public final class BukkitConsoleSoul extends ConsoleSoul<CommandSender> {

    private final Audience audience;

    /**
     * Construct a ConsoleSoul
     *
     * @param audience Adventure audience
     */
    public BukkitConsoleSoul(final @NonNull Audience audience) {
        this.audience = audience;
    }

    @Override
    public boolean hasPermission(@NonNull final String permission) {
        return true;
    }

    @Override
    public @NotNull CommandSender asSender() {
        return Bukkit.getConsoleSender();
    }

    @Override
    public @NonNull Audience audience() {
        return this.audience;
    }

}
