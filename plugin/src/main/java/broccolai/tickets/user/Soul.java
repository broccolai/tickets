package broccolai.tickets.user;

import broccolai.corn.spigot.CornUser;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public interface Soul extends CornUser {

    /**
     * Get the souls name
     *
     * @return Name
     */
    @NonNull
    String getName();

    /**
     * Get the souls unique id
     *
     * @return Unique id
     */
    @NonNull
    UUID getUniqueId();

    /**
     * Get the Command Sender associated with the soul
     *
     * @return Command Sender
     */
    @Nullable
    CommandSender asSender();

}
