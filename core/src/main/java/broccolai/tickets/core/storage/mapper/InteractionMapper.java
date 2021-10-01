package broccolai.tickets.core.storage.mapper;

import broccolai.tickets.api.model.interaction.Action;
import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.core.model.interaction.BasicInteraction;
import broccolai.tickets.core.model.interaction.BasicMessageInteraction;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.EnumMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public final class InteractionMapper implements RowMapper<Interaction> {

    @Override
    public Interaction map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        ColumnMapper<Action> actionMapper = EnumMapper.byName(Action.class);
        ColumnMapper<LocalDateTime> dateMapper = ctx.findColumnMapperFor(LocalDateTime.class)
                .orElseThrow(IllegalStateException::new);
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class)
                .orElseThrow(IllegalStateException::new);

        Action action = actionMapper.map(rs, "action", ctx);
        LocalDateTime date = dateMapper.map(rs, "time", ctx);
        UUID sender = uuidMapper.map(rs, "sender", ctx);
        String message = rs.getString("message");

        if (message != null) {
            return new BasicMessageInteraction(action, date, sender, message);
        }

        return new BasicInteraction(action, date, sender);
    }

}
