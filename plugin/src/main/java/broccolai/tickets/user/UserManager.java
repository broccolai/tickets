package broccolai.tickets.user;

import broccolai.tickets.storage.SQLManager;
import broccolai.tickets.storage.functions.SettingsSQL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * The manager for users.
 */
public class UserManager {
    @NotNull
    private final Map<UUID, UserSettings> users = new HashMap<>();
    @NotNull
    private final SettingsSQL settingsSQL;

    public UserManager(@NotNull SQLManager sqlManager) {
        this.settingsSQL = sqlManager.getSetting();
    }

    /**
     * Retrieve a UserSettings instance.
     * @param uuid the unique id
     * @return a constructed UserSettings instance
     */
    @NotNull
    public UserSettings get(@NotNull UUID uuid) {
        UserSettings settings = users.get(uuid);

        if (settings != null) {
            return settings;
        }

        if (settingsSQL.exists(uuid)) {
            settings = settingsSQL.select(uuid);
        } else {
            settings = new UserSettings(true);
            settingsSQL.insert(uuid, settings);
        }

        users.put(uuid, settings);
        return settings;
    }

    /**
     * Update a UserSettings instance and save it.
     * @param uuid the unique id
     * @param action the function to apply to the Users Settings
     */
    public void update(@NotNull UUID uuid, @NotNull Function<UserSettings, UserSettings> action) {
        UserSettings settings = get(uuid);
        settings = action.apply(settings);

        users.put(uuid, settings);
        settingsSQL.update(uuid, settings);
    }
}