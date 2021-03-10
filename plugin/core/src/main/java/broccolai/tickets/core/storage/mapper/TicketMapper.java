package broccolai.tickets.core.storage.mapper;

import broccolai.tickets.api.model.interaction.Action;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.core.model.interaction.BasicMessageInteraction;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.EnumMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TicketMapper implements RowMapper<Ticket> {

    @Override
    public Ticket map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class).orElseThrow(IllegalStateException::new);
        ColumnMapper<Position> positionMapper = ctx.findColumnMapperFor(Position.class).orElseThrow(IllegalStateException::new);
        ColumnMapper<LocalDateTime> timeMapper = ctx
                .findColumnMapperFor(LocalDateTime.class)
                .orElseThrow(IllegalStateException::new);

        int id = rs.getInt("id");
        UUID player = uuidMapper.map(rs, "player", ctx);
        Position position = positionMapper.map(rs, "position", ctx);
        TicketStatus status = EnumMapper.byName(TicketStatus.class).map(rs, "status", ctx);
        UUID picker = uuidMapper.map(rs, "picker", ctx);

        Action action = EnumMapper.byName(Action.class).map(rs, "action", ctx);
        LocalDateTime time = timeMapper.map(rs, "time", ctx);
        UUID sender = uuidMapper.map(rs, "sender", ctx);
        String message = rs.getString("message");

        MessageInteraction messageInteraction = new BasicMessageInteraction(action, time, sender, message);

        return new Ticket(id, player, position, status, messageInteraction, picker);
    }

}

