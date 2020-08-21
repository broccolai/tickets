package broccolai.tickets.user;

import broccolai.tickets.storage.SQLManager;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class UserManager {
    private final HashMap<UUID, UserSettings> users = new HashMap<>();
    private final SQLManager sqlManager;

    public UserManager(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public @NotNull UserSettings get(UUID uuid) {
        UserSettings settings = users.get(uuid);

        if (settings != null) {
            return settings;
        }

        if (sqlManager.getSetting().exists(uuid)) {
            settings = sqlManager.getSetting().select(uuid);
        } else {
            settings = new UserSettings(true);
            sqlManager.getSetting().insert(uuid, settings);
        }

        users.put(uuid, settings);
        return settings;
    }

    public void update(UUID uuid, Function<UserSettings, UserSettings> action) {
        UserSettings settings = get(uuid);
        settings = action.apply(settings);

        users.put(uuid, settings);
        sqlManager.getSetting().update(uuid, settings);
    }
}