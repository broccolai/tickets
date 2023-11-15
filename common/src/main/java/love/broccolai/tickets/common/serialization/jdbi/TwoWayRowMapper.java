package love.broccolai.tickets.common.serialization.jdbi;

import org.jdbi.v3.core.mapper.RowViewMapper;
import org.jdbi.v3.core.statement.SqlStatement;

public interface TwoWayRowMapper<T> extends RowViewMapper<T> {

    <S extends SqlStatement<S>> SqlStatement<S> bindToStatement(S statement, T action);

}
