package broccolai.tickets.storage.platforms;

import broccolai.tickets.configuration.Config;
import co.aikar.idb.DbRow;
import org.bukkit.plugin.Plugin;

public interface Platform {
    void setup(Plugin plugin, Config config);

    Long getPureLong(DbRow row, String column);

    Integer getPureInteger(Object value);
}
