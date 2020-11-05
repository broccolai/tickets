package broccolai.tickets.user;

import broccolai.tickets.locale.LocaleManager;
import broccolai.tickets.utilities.Dirtyable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public final class PlayerSoul extends Soul implements Dirtyable {

    private final UserManager userManager;

    private final @NonNull Player player;
    private final @NonNull UUID uniqueId;
    private @Nullable UserSettings settings = null;

    private boolean dirty = false;

    /**
     * Construct a PlayerSoul with a Player instance
     *
     * @param userManager   User manager
     * @param localeManager Locale manager instance
     * @param player        Player instance
     */
    public PlayerSoul(
            final @NonNull UserManager userManager,
            final @NonNull LocaleManager localeManager,
            final @NonNull Player player
    ) {
        super(localeManager);
        this.userManager = userManager;
        this.player = player;
        this.uniqueId = player.getUniqueId();
    }

    @Override
    public @NonNull String getName() {
        return Objects.requireNonNull(player.getName());
    }

    @Override
    public @NonNull UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public @NonNull CommandSender asSender() {
        return asPlayer();
    }

    @Override
    public void message(final @NonNull String message) {
        asPlayer().sendMessage(message);
    }

    /**
     * Get the Player associated with this soul
     *
     * @return Player object
     */
    public @NonNull Player asPlayer() {
        return player;
    }

    /**
     * Retrieve a UserSettings instance.
     *
     * @return a constructed UserSettings instance
     */
    public @NonNull UserSettings preferences() {
        if (settings != null) {
            return settings;
        }

        this.settings = this.userManager.loadSettings(uniqueId);
        return this.settings;
    }

    /**
     * Update a UserSettings instance and save it.
     *
     * @param action the function to apply to the Users Settings
     */
    public void modifyPreferences(final @NonNull Consumer<UserSettings> action) {
        UserSettings settings = preferences();
        action.accept(settings);

        dirty = true;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

}
