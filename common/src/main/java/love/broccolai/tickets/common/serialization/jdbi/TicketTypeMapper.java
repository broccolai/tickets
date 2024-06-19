package love.broccolai.tickets.common.serialization.jdbi;

import com.google.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.registry.TicketTypeRegistry;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketTypeMapper extends AbstractArgumentFactory<TicketFormat> implements ColumnMapper<TicketFormat> {

    private final TicketTypeRegistry typeRegistry;

    @Inject
    public TicketTypeMapper(final TicketTypeRegistry typeRegistry) {
        super(Types.VARCHAR);
        this.typeRegistry = typeRegistry;
    }

    @Override
    public TicketFormat map(final ResultSet rs, final int columnNumber, final StatementContext ctx) throws SQLException {
        String identifier = rs.getString(columnNumber);

        return this.typeRegistry.fromIdentifier(identifier);
    }

    @Override
    protected Argument build(TicketFormat value, ConfigRegistry config) {
        return (position, statement, ctx) -> {
            statement.setString(position, value.identifier());
        };
    }
}
