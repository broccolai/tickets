package broccolai.tickets.core.configuration;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("CanBeFinal")
public final class AdvancedConfiguration {

    public String api = "https://tickets.broccol.ai";

    public int openTicketLimit = 5;

}
