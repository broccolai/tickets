package love.broccolai.tickets.common.serialization.jdbi;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.TicketType;
import love.broccolai.tickets.common.configuration.TicketsConfiguration;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketTypeMapper implements ColumnMapper<TicketType> {

    private final TicketsConfiguration configuration;

    @Inject
    public TicketTypeMapper(final TicketsConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public TicketType map(final ResultSet rs, final int columnNumber, final StatementContext ctx) throws SQLException {
        String identifier = rs.getString(columnNumber);

        return Trove.of(this.configuration.types)
            .first(type -> Objects.equal(identifier, type.identifier()))
            .orElseThrow();
    }
}
