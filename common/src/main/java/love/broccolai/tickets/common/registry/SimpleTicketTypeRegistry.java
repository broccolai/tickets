package love.broccolai.tickets.common.registry;

import com.google.inject.Singleton;
import love.broccolai.tickets.api.model.TicketType;
import love.broccolai.tickets.api.registry.TicketTypeRegistry;

@Singleton
public class SimpleTicketTypeRegistry extends BidirectionalMappedRegistry<TicketType> implements TicketTypeRegistry {

}
