package broccolai.tickets.user;

import broccolai.corn.spigot.CornUser;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Soul extends CornUser {
    @NotNull
    String getName();

    @NotNull
    UUID getUniqueId();

    @Nullable
    CommandSender asSender();
}
