package co.uk.magmo.puretickets.storage.FunctionInterfaces;

import java.util.UUID;

public interface SettingsFunctions {
    UserSettings select(UUID uuid);

    Boolean exists(UUID uuid);

    void insert(UUID uuid, UserSettings settings);

    void update(UUID uuid, UserSettings settings);
}

