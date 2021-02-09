package broccolai.tickets.core.storage.mapper;

import broccolai.tickets.core.message.Message;
import broccolai.tickets.core.message.MessageReason;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.EnumMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public final class MessageMapper implements RowMapper<Message> {

    @Override
    public Message map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        ColumnMapper<MessageReason> reasonMapper = EnumMapper.byName(MessageReason.class);
        ColumnMapper<LocalDateTime> dateMapper = ctx.findColumnMapperFor(LocalDateTime.class)
                .orElseThrow(IllegalStateException::new);
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class)
                .orElseThrow(IllegalStateException::new);

        MessageReason reason = reasonMapper.map(rs, "reason", ctx);
        LocalDateTime date = dateMapper.map(rs, "date", ctx);
        String data = rs.getString("data");
        UUID sender;

        if (rs.getString("sender") == null || rs.getString("sender").equals("null")) {
            sender = null;
        } else {
            sender = uuidMapper.map(rs, "sender", ctx);
        }

        return Message.load(reason, date, data, sender);
    }

}
