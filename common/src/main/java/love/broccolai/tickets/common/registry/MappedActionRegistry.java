package love.broccolai.tickets.common.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Singleton;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.registry.ActionRegistry;

@Singleton
public class MappedActionRegistry implements ActionRegistry {

    private BiMap<String, Class<? extends Action>> actionTypeRegistry = HashBiMap.create();

    @Override
    public void register(String typeIdentifier, Class<? extends Action> type) {
        this.actionTypeRegistry.put(typeIdentifier, type);
    }

    @Override
    public Class<? extends Action> typeFromIdentifier(String identifier) {
        return this.actionTypeRegistry.get(identifier);
    }

    @Override
    public String identifierFromType(Class<? extends Action> type) {
        return this.actionTypeRegistry.inverse().get(type);
    }
}
