package love.broccolai.tickets.common.packaged;

import love.broccolai.tickets.api.model.action.packaged.AssignAction;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.api.registry.ActionRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PackagedActions {
    private PackagedActions() {
    }

    public static void register(final ActionRegistry actionRegistry) {
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
    }
}
