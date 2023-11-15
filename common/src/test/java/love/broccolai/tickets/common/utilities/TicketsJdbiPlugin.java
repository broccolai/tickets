package love.broccolai.tickets.common.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Instant;
import love.broccolai.tickets.common.serialization.gson.InstantAdapter;
import love.broccolai.tickets.common.serialization.jdbi.ActionMapper;
import love.broccolai.tickets.common.serialization.jdbi.TicketMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.gson2.Gson2Config;

public final class TicketsJdbiPlugin implements JdbiPlugin {

    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(Instant.class, new InstantAdapter())
        .create();

    @Override
    public void customizeJdbi(final Jdbi jdbi) {
        jdbi
            .registerRowMapper(new TicketMapper())
            .registerRowMapper(new ActionMapper(PremadeActionRegistry.create()));

        jdbi.getConfig(Gson2Config.class).setGson(GSON);
    }

}
