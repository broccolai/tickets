package co.uk.magmo.puretickets.storage.platforms;

import co.aikar.idb.DbRow;
import co.uk.magmo.puretickets.configuration.Config;
import org.bukkit.plugin.Plugin;

public interface Platform {
    void setup(Plugin plugin, Config config);

    Long getPureLong(DbRow row, String column);

    Integer getPureInteger(Object value);
}
