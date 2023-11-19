package love.broccolai.tickets.common.serialization.jdbi;

import com.google.inject.Inject;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.registry.ActionRegistry;
import org.jdbi.v3.core.qualifier.QualifiedType;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.core.statement.SqlStatement;
import org.jdbi.v3.json.Json;

public final class ActionMapper implements TwoWayRowMapper<Action> {

    private final ActionRegistry registry;

    @Inject
    public ActionMapper(final ActionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Action map(RowView rowView) {
        String identifier = rowView.getColumn("type", String.class);

        QualifiedType<? extends Action> qualifiedType = this.qualifiedType(
            this.registry.typeFromIdentifier(identifier)
        );

        return rowView.getColumn("data", qualifiedType);
    }

    @Override
    public <S extends SqlStatement<S>> SqlStatement<S> bindToStatement(S statement, Action action) {
        String identifier = this.registry.identifierFromType(action.getClass());

        QualifiedType<? extends Action> qualifiedType = this.qualifiedType(action.getClass());

        return statement
            .bind("type", identifier)
            .bindByType("data", action, qualifiedType);
    }

    private QualifiedType<? extends Action> qualifiedType(Class<? extends Action> type) {
        return QualifiedType.of(type).with(Json.class);
    }
}
