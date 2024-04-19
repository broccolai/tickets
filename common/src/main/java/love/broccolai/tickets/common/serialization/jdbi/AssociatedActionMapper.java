package love.broccolai.tickets.common.serialization.jdbi;

import com.google.inject.Inject;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.AssociatedAction;
import love.broccolai.tickets.api.registry.ActionRegistry;
import org.jdbi.v3.core.mapper.RowViewMapper;
import org.jdbi.v3.core.qualifier.QualifiedType;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.json.Json;

public final class AssociatedActionMapper implements RowViewMapper<AssociatedAction> {

    private final ActionRegistry registry;

    @Inject
    public AssociatedActionMapper(final ActionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public AssociatedAction map(RowView rowView) {
        int ticket = rowView.getColumn("ticket", Integer.class);
        String identifier = rowView.getColumn("type", String.class);

        QualifiedType<? extends Action> qualifiedType = this.qualifiedType(
            this.registry.fromIdentifier(identifier)
        );

        Action action = rowView.getColumn("data", qualifiedType);

        return new AssociatedAction(ticket, action);
    }

    private QualifiedType<? extends Action> qualifiedType(Class<? extends Action> type) {
        return QualifiedType.of(type).with(Json.class);
    }
}
