package love.broccolai.tickets.minecraft.common.parsers.ticket;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import io.leangen.geantyref.TypeToken;
import java.util.Set;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SelfTicketDescriptor implements ParserDescriptor<Commander, Ticket> {

    private final TicketParser ticketParser;
    private final Set<TicketStatus> statuses;

    @AssistedInject
    public SelfTicketDescriptor(
        final StorageService storageService,
        final @Assisted("statuses") Set<TicketStatus> statuses
    ) {
        this.ticketParser = new TicketParser(storageService);
        this.statuses = statuses;
    }

    @Override
    public ArgumentParser<Commander, Ticket> parser() {
        return this.ticketParser;
    }

    @Override
    public TypeToken<Ticket> valueType() {
        return TypeToken.get(Ticket.class);
    }
}
