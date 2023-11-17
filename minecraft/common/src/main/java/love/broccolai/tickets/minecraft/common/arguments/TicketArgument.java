package love.broccolai.tickets.minecraft.common.arguments;

import cloud.commandframework.arguments.CommandArgument;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import java.util.Set;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.parsers.TicketIdentifierParser;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TicketArgument extends CommandArgument<Commander, Ticket> {

    @AssistedInject
    public TicketArgument(
        final StorageService storageService,
        final @Assisted("name") String name,
        final @Assisted("statuses") Set<TicketStatus> statuses
    ) {
        super(name, new TicketIdentifierParser(storageService, TicketIdentifierParser.ParserMode.SOURCED, statuses), Ticket.class);
    }

}
