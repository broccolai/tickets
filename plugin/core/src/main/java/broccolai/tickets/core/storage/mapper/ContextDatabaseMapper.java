package broccolai.tickets.core.storage.mapper;

import broccolai.tickets.api.service.context.ContextService;
import broccolai.tickets.core.model.context.ContextKeyValuePair;
import cloud.commandframework.types.tuples.Pair;
import com.google.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public final class ContextDatabaseMapper implements RowMapper<ContextKeyValuePair<?>> {

    private final ContextService contextService;

    @Inject
    public ContextDatabaseMapper(final @NonNull ContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public ContextKeyValuePair<?> map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        String namespace = rs.getString("namespace");
        String name = rs.getString("namespace");
        String serialized = rs.getString("value");

        return this.contextService.parseKey(namespace, name)
                .flatMap(key -> this.contextService.useMapper(key).map(mapper -> Pair.of(key, mapper)))
                .map(pair -> new ContextKeyValuePair<>(pair.getFirst(), pair.getSecond().deserialize(serialized)))
                .orElse(null);
    }

}
