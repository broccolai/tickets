package love.broccolai.tickets.common.utilities;

import love.broccolai.tickets.api.model.action.packaged.AssignAction;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.api.registry.ActionRegistry;
import love.broccolai.tickets.common.registry.SimpleActionRegistry;

public final class PremadeActionRegistry {
    private PremadeActionRegistry() {
    }

    public static ActionRegistry create() {
        ActionRegistry actionRegistry = new SimpleActionRegistry();

        actionRegistry.register(
            OpenAction.IDENTIFIER,
            OpenAction.class
        );
        actionRegistry.register(
            CloseAction.IDENTIFIER,
            CloseAction.class
        );
        actionRegistry.register(
            CommentAction.IDENTIFIER,
            CommentAction.class
        );
        actionRegistry.register(
            AssignAction.IDENTIFIER,
            AssignAction.class
        );

        return actionRegistry;
    }
}
