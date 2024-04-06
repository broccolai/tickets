package love.broccolai.tickets.common.utilities;

import love.broccolai.tickets.api.model.TicketType;
import love.broccolai.tickets.api.model.action.packaged.AssignAction;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.api.registry.ActionRegistry;
import love.broccolai.tickets.api.registry.TicketTypeRegistry;
import love.broccolai.tickets.common.configuration.TicketsConfiguration;
import love.broccolai.tickets.common.registry.SimpleActionRegistry;
import love.broccolai.tickets.common.registry.SimpleTicketTypeRegistry;

public final class PremadeTicketTypeRegistry {
    private PremadeTicketTypeRegistry() {
    }

    public static TicketTypeRegistry create() {
        TicketTypeRegistry typeRegistry = new SimpleTicketTypeRegistry();
        TicketsConfiguration configuration = new TicketsConfiguration();

        configuration.types.forEach(type -> {
            typeRegistry.register(type.identifier(), type);
        });

        return typeRegistry;
    }
}
