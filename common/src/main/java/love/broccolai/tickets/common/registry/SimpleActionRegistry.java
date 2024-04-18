package love.broccolai.tickets.common.registry;

import com.google.inject.Singleton;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.registry.ActionRegistry;

@Singleton
public class SimpleActionRegistry extends BidirectionalMappedRegistry<Class<? extends Action>> implements ActionRegistry {

}
