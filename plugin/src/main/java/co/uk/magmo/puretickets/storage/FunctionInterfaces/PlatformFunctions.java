package co.uk.magmo.puretickets.storage.FunctionInterfaces;

import co.aikar.idb.DbRow;
import co.uk.magmo.puretickets.configuration.Config;
import org.bukkit.plugin.Plugin;

public interface PlatformFunctions {
    void setup(Plugin plugin, Config config);

    Long getPureLong(DbRow row, String column);
}
