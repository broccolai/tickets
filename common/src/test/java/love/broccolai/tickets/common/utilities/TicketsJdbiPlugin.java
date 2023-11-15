package love.broccolai.tickets.common.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Instant;
import love.broccolai.tickets.common.storage.ActionMapper;
import love.broccolai.tickets.common.storage.InstantAdapter;
import love.broccolai.tickets.common.storage.JsonArgumentFactory;
import love.broccolai.tickets.common.storage.TicketMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.gson2.Gson2Config;

public final class TicketsJdbiPlugin implements JdbiPlugin {

    private static final Gson GSON = new GsonBuilder()
        .setLenient()
        .registerTypeAdapter(Instant.class, new InstantAdapter())
        .create();

    @Override
    public void customizeJdbi(final Jdbi jdbi) {
        jdbi
            .registerArgument(new JsonArgumentFactory())
            .registerRowMapper(new TicketMapper())
            .registerRowMapper(new ActionMapper(PremadeActionRegistry.create()));

        jdbi.getConfig(Gson2Config.class).setGson(GSON);
    }

}
