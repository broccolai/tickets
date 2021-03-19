package broccolai.tickets.core.configuration;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("CanBeFinal")
public class DiscordConfiguration {

    public boolean enabled = false;

    public String guild = "";

    public String token = "";

    public String name = null;

}
