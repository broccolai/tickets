package broccolai.tickets.bukkit.model;

import broccolai.corn.context.ContextKey;
import broccolai.corn.context.DelegatingContextKeyRegistry;
import org.bukkit.Location;

public final class BukkitTicketContextKeys extends DelegatingContextKeyRegistry {

    public static final ContextKey<Location> LOCATION = ContextKey.of("tickets-bukkit", "location", Location.class);

    public BukkitTicketContextKeys() {
        this.register(LOCATION);
    }

}
