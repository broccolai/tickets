package broccolai.tickets.user;

import broccolai.tickets.storage.functions.SettingsSQL;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSoul implements Soul {
    @NotNull
    protected final Player player;
    @NotNull
    protected final UUID uniqueId;
    @Nullable
    private UserSettings settings = null;

    public PlayerSoul(@NotNull Player player) {
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
    @Nullable
    public CommandSender asSender() {
        return asPlayer();
    }

    @Override
    public void message(@NotNull String message) {
        asPlayer().sendMessage(message);
    }

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
    public void modifyPreferences(@NotNull Consumer<UserSettings> action) {
        UserSettings settings = preferences();
        action.accept(settings);

        SettingsSQL.update(uniqueId, settings);
    }
}
