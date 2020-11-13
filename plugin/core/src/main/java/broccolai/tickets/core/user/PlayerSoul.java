package broccolai.tickets.core.user;

import broccolai.tickets.core.utilities.Dirtyable;
import broccolai.tickets.core.utilities.TicketLocation;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Consumer;

public abstract class PlayerSoul<C, P extends C> implements Soul<C>, Dirtyable {

    private final UserManager<C, P, ?> userManager;
    private @Nullable UserSettings settings = null;
    private boolean dirty = false;

    private final Audience audience;
    protected final P player;

    /**
     * Construct a PlayerSoul with a Player instance
     *
     * @param userManager User manager
     * @param audience    Adventure audience
     * @param player      Player instance
     */
    public PlayerSoul(
            final @NonNull UserManager<C, P, ?> userManager,
            final @NonNull Audience audience,
            final @NonNull P player
    ) {
        this.userManager = userManager;
        this.audience = audience;
        this.player = player;
    }

    /**
     * @return Get current location of player
     */
    public abstract @NonNull TicketLocation currentLocation();

    /**
     * @param location Teleport to location
     */
    public abstract void teleport(@NonNull TicketLocation location);

    @Override
    public final @NonNull C asSender() {
        return this.asPlayer();
    }

    /**
     * Get the Player associated with this soul
     *
     * @return Player object
     */
    public final @NonNull P asPlayer() {
        return this.player;
    }

    /**
     * Retrieve a UserSettings instance.
     *
     * @return a constructed UserSettings instance
     */
    public final @NonNull UserSettings preferences() {
        if (this.settings != null) {
            return this.settings;
        }

        this.settings = this.userManager.loadSettings(this.getUniqueId());
        return this.settings;
    }

    /**
     * Update a UserSettings instance and save it.
     *
     * @param action the function to apply to the Users Settings
     */
    public final void modifyPreferences(final @NonNull Consumer<UserSettings> action) {
        UserSettings settings = preferences();
        action.accept(settings);

        this.dirty = true;
    }

    @Override
    public final @NonNull Audience audience() {
        return this.audience;
    }

    @Override
    public final boolean isDirty() {
        return this.dirty;
    }

}
