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

    @NonNull
    private final Player player;
    @NonNull
    private final UUID uniqueId;
    @Nullable
    private UserSettings settings = null;

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
    @NonNull
    public String getName() {
        return Objects.requireNonNull(player.getName());
    }

    @Override
    @NonNull
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    @NonNull
    public CommandSender asSender() {
        return asPlayer();
    }

    @Override
    public void message(@NonNull final String message) {
        asPlayer().sendMessage(message);
    }

    /**
     * Get the Player associated with this soul
     *
     * @return Player object
     */
    @NonNull
    public Player asPlayer() {
        return player;
    }

    /**
     * Retrieve a UserSettings instance.
     *
     * @return a constructed UserSettings instance
     */
    @NonNull
    public UserSettings preferences() {
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
    public void modifyPreferences(@NonNull final Consumer<UserSettings> action) {
        UserSettings settings = preferences();
        action.accept(settings);

        dirty = true;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

}
