package co.uk.magmo.puretickets;

import co.uk.magmo.puretickets.configuration.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class PureTickets extends JavaPlugin {
    @Override
    public void onEnable() {
        Config config = new Config(this);
    }

    @Override
    public void onDisable() { }
}
