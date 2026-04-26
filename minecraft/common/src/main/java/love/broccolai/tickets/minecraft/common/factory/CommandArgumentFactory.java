package love.broccolai.tickets.minecraft.common.factory;

import com.google.inject.assistedinject.Assisted;
import java.util.Set;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.minecraft.common.parsers.ProfileDescriptor;
import love.broccolai.tickets.minecraft.common.parsers.ticket.SelfTicketDescriptor;
import love.broccolai.tickets.minecraft.common.parsers.ticket.TargetTicketDescriptor;
import love.broccolai.tickets.minecraft.common.parsers.ticket.TicketDescriptor;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CommandArgumentFactory {

    ProfileDescriptor profile();

    TicketDescriptor ticket(@Assisted("statuses") Set<TicketStatus> statuses);

    SelfTicketDescriptor selfTicket(@Assisted("statuses") Set<TicketStatus> statuses);

    TargetTicketDescriptor targetedTicket(@Assisted("statuses") Set<TicketStatus> statuses);
}
