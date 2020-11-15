package broccolai.tickets.core.user;

import broccolai.tickets.core.exceptions.PureException;
import com.google.gson.JsonObject;
import net.kyori.adventure.audience.ForwardingAudience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public interface Soul<C> extends ForwardingAudience.Single {

    /**
     * Get the souls name
     *
     * @return Name
     */
    @NonNull String getName();

    /**
     * Get the souls unique id
     *
     * @return Unique id
     */
    @NonNull UUID getUniqueId();

    /**
     * Handle a {@link PureException}
     *
     * @param exception Exception to handle
     */
    default void handleException(final @NonNull PureException exception) {
        this.sendMessage(exception.get());
    }

    /**
     * Check if the soul has a certain permission
     *
     * @param permission Permission to check
     * @return Boolean
     */
    boolean hasPermission(@NonNull String permission);

    /**
     * Get the Command Sender associated with the soul
     *
     * @return Command Sender
     */
    @Nullable C asSender();

    /**
     * @return Json representation
     */
    default @NonNull JsonObject toJson() {
        JsonObject json = new JsonObject();

        json.addProperty("name", this.getName());
        json.addProperty("uuid", this.getUniqueId().toString());

        return json;
    }

}
