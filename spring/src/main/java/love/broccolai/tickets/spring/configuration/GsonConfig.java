package love.broccolai.tickets.spring.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Instant;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.spring.serialization.ActionSerializer;
import love.broccolai.tickets.spring.serialization.InstantAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .registerTypeAdapter(Action.class, new ActionSerializer())
            .create();
    }
}
