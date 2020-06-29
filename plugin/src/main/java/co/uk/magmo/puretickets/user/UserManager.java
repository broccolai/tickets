package co.uk.magmo.puretickets.user;

import co.uk.magmo.puretickets.storage.SQLManager;
import java.util.*;
import java.util.function.Function;

public class UserManager {
    private final HashMap<UUID, UserSettings> users = new HashMap<>();
    private final SQLManager sqlManager;

    public UserManager(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public UserSettings get(UUID uuid) {
        UserSettings settings = users.get(uuid);

        if (settings != null) {
            return settings;
        }

        if (sqlManager.getSetting().exists(uuid)) {
            settings = sqlManager.getSetting().select(uuid);
            users.put(uuid, settings);
        } else {
            settings = new UserSettings(true);
            sqlManager.getSetting().insert(uuid, settings);
        }

        return settings;
    }

    public void update(UUID uuid, Function<UserSettings, UserSettings> action) {
        UserSettings settings = users.get(uuid);
        settings = action.apply(settings);

        users.put(uuid, settings);
        sqlManager.getSetting().update(uuid, settings);
    }
}