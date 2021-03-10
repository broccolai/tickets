package broccolai.tickets.api.service.intergrations;

import com.google.gson.JsonObject;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface DiscordService {

    void announce(@NonNull JsonObject jsonObject);

}
