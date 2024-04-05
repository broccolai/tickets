package love.broccolai.tickets.common.configuration;

import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
public class DatabaseConfiguration implements Configuration {

    public String path = "storage.db";

}
