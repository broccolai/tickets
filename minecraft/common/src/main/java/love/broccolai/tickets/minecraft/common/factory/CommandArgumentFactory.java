package love.broccolai.tickets.minecraft.common.factory;

import com.google.inject.assistedinject.Assisted;
import java.util.Set;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.minecraft.common.arguments.ProfileArgument;
import love.broccolai.tickets.minecraft.common.arguments.ProfileTicketArgument;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CommandArgumentFactory {

    ProfileArgument profile(
        @Assisted("name") String name
    );

    ProfileTicketArgument profileTicket(
        @Assisted("name") String name,
        @Assisted("statuses") Set<TicketStatus> statuses
    );

}
