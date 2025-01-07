package love.broccolai.tickets.common.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Instant;
import love.broccolai.tickets.api.model.format.TicketFormatContent;
import love.broccolai.tickets.api.registry.TicketTypeRegistry;
import love.broccolai.tickets.common.serialization.gson.InstantAdapter;
import love.broccolai.tickets.common.serialization.gson.TicketFormatContentAdapter;
import love.broccolai.tickets.common.serialization.jdbi.ActionMapper;
import love.broccolai.tickets.common.serialization.jdbi.TicketMapper;
import love.broccolai.tickets.common.serialization.jdbi.TicketTypeMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.gson2.Gson2Config;
import org.jdbi.v3.gson2.Gson2Plugin;

public final class TicketsJdbiPlugin implements JdbiPlugin {

    private static final TicketTypeRegistry TICKET_TYPE_REGISTRY = PremadeTicketTypeRegistry.create();

    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(TicketFormatContent.class, new TicketFormatContentAdapter(TICKET_TYPE_REGISTRY))
        .registerTypeAdapter(Instant.class, new InstantAdapter())
        .create();

    @Override
    public void customizeJdbi(final Jdbi jdbi) {
        TicketTypeMapper ticketTypeMapper = new TicketTypeMapper(TICKET_TYPE_REGISTRY);

        jdbi
            .installPlugin(new Gson2Plugin())
            .registerRowMapper(new TicketMapper())
            .registerRowMapper(new ActionMapper(PremadeActionRegistry.create()))
            .registerColumnMapper(ticketTypeMapper)
            .registerArgument(ticketTypeMapper);

        jdbi.getConfig(Gson2Config.class).setGson(GSON);
    }

}
