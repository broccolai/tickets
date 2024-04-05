package love.broccolai.tickets.common.serialization.jdbi;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketType;
import love.broccolai.tickets.common.model.SimpleTicket;
import org.jdbi.v3.core.mapper.RowViewMapper;
import org.jdbi.v3.core.result.RowView;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketMapper implements RowViewMapper<Ticket> {

    @Override
    public Ticket map(RowView row) {
        return new SimpleTicket(
            row.getColumn("id", Integer.class),
            row.getColumn("type_identifier", TicketType.class),
            row.getColumn("creator", UUID.class),
            row.getColumn("date", Instant.class),
            new LinkedHashSet<>()
        );
    }
}
