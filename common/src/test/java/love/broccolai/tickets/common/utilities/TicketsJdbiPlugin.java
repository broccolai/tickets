package love.broccolai.tickets.common.utilities;

import love.broccolai.tickets.common.storage.DelegatingActionMapper;
import love.broccolai.tickets.common.storage.TicketMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;

public final class TicketsJdbiPlugin implements JdbiPlugin {

    @Override
    public void customizeJdbi(final Jdbi jdbi) {
        jdbi
            .registerRowMapper(new TicketMapper())
            .registerRowMapper(new DelegatingActionMapper());
    }

}
