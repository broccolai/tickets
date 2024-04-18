package love.broccolai.tickets.common.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Instant;
import love.broccolai.tickets.common.serialization.gson.InstantAdapter;
import love.broccolai.tickets.common.serialization.jdbi.ActionMapper;
import love.broccolai.tickets.common.serialization.jdbi.TicketMapper;
import love.broccolai.tickets.common.serialization.jdbi.TicketTypeMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.gson2.Gson2Config;
import org.jdbi.v3.gson2.Gson2Plugin;

public final class TicketsJdbiPlugin implements JdbiPlugin {

    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(Instant.class, new InstantAdapter())
        .create();

    @Override
    public void customizeJdbi(final Jdbi jdbi) {
        TicketTypeMapper ticketTypeMapper = new TicketTypeMapper(PremadeTicketTypeRegistry.create());

        jdbi
            .installPlugin(new Gson2Plugin())
            .registerRowMapper(new TicketMapper())
            .registerRowMapper(new ActionMapper(PremadeActionRegistry.create()))
            .registerColumnMapper(ticketTypeMapper)
            .registerArgument(ticketTypeMapper);

        jdbi.getConfig(Gson2Config.class).setGson(GSON);
    }

}
