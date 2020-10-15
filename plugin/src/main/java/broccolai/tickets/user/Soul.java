package broccolai.tickets.user;

import broccolai.corn.spigot.CornUser;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Soul extends CornUser {

    /**
     * Get the souls name
     *
     * @return Name
     */
    @NotNull
    String getName();

    /**
     * Get the souls unique id
     *
     * @return Unique id
     */
    @NotNull
    UUID getUniqueId();

    /**
     * Get the Command Sender associated with the soul
     *
     * @return Command Sender
     */
    @Nullable
    CommandSender asSender();

}
