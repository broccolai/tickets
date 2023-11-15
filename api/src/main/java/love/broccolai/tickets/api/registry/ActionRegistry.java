package love.broccolai.tickets.api.registry;

import love.broccolai.tickets.api.model.action.Action;

public interface ActionRegistry {

    void register(String identifier, Class<? extends Action> type);

    Class<? extends Action> typeFromIdentifier(String identifier);

    String identifierFromType(Class<? extends Action> type);

}
