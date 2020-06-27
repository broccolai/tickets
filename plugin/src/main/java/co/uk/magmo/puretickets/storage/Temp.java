package co.uk.magmo.puretickets.storage;

import co.uk.magmo.puretickets.storage.FunctionInterfaces.*;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class Temp extends SQLManager {
    public Temp(MiscellaneousFunctions miscellaneous, TicketFunctions ticket, MessageFunctions message, NotificationFunctions notification, SettingsFunctions setting, HelperFunctions helpers) {
        super(miscellaneous, ticket, message, notification, setting, helpers);
    }

    @Override
    public void setup(Plugin plugin) {

    }

    @Override
    public HashMap<UUID, Integer> highscores(TimeAmount amount) {
        return null;
    }


}
