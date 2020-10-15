package broccolai.tickets.user;

import broccolai.tickets.storage.functions.SettingsSQL;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public final class PlayerSoul implements Soul {

    @NotNull
    private final Player player;
    @NotNull
    private final UUID uniqueId;
    @Nullable
    private UserSettings settings = null;

    /**
     * Construct a PlayerSoul with a Player instance
     *
     * @param player Player instance
     */
    public PlayerSoul(@NotNull final Player player) {
        this.player = player;
        this.uniqueId = player.getUniqueId();
    }

    @Override
    @NotNull
    public String getName() {
        return Objects.requireNonNull(player.getName());
    }

    @Override
    @NotNull
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    @NotNull
    public CommandSender asSender() {
        return asPlayer();
    }

    @Override
    public void message(@NotNull final String message) {
        asPlayer().sendMessage(message);
    }

    /**
     * Get the Player associated with this soul
     *
     * @return Player object
     */
    @NotNull
    public Player asPlayer() {
        return player;
    }

    /**
     * Retrieve a UserSettings instance.
     *
     * @return a constructed UserSettings instance
     */
    @NotNull
    public UserSettings preferences() {
        if (settings != null) {
            return settings;
        }

        if (SettingsSQL.exists(uniqueId)) {
            settings = SettingsSQL.select(uniqueId);
        } else {
            settings = new UserSettings(true);
            SettingsSQL.insert(uniqueId, settings);
        }

        return settings;
    }

    /**
     * Update a UserSettings instance and save it.
     *
     * @param action the function to apply to the Users Settings
     */
    public void modifyPreferences(@NotNull final Consumer<UserSettings> action) {
        UserSettings settings = preferences();
        action.accept(settings);

        SettingsSQL.update(uniqueId, settings);
    }

}
