package co.uk.magmo.puretickets.storage.FunctionInterfaces;

import co.uk.magmo.puretickets.storage.TimeAmount;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public interface MiscellaneousFunctions {
    void setup(Plugin plugin);

    HashMap<UUID, Integer> highscores(TimeAmount amount);
}