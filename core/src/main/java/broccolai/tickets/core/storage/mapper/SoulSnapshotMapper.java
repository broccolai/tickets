package broccolai.tickets.core.storage.mapper;

import broccolai.tickets.api.model.user.SoulSnapshot;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public final class SoulSnapshotMapper implements RowMapper<SoulSnapshot> {

    @Override
    public SoulSnapshot map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class)
                .orElseThrow(IllegalStateException::new);

        UUID uuid = uuidMapper.map(rs, "uuid", ctx);
        String username = rs.getString("username");

        return new SoulSnapshot(uuid, username);
    }

}
