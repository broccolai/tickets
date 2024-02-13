package love.broccolai.tickets.minecraft.common.parsers.ticket;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import io.leangen.geantyref.TypeToken;
import java.util.List;
import java.util.Set;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.parsers.ProfileDescriptor;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.parser.aggregate.AggregateParser;
import org.incendo.cloud.parser.aggregate.AggregateResultMapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TargetTicketDescriptor implements AggregateParser<Commander, Ticket>, ParserDescriptor<Commander, Ticket> {

    private static final CloudKey<Profile> TARGET_KEY = CloudKey.cloudKey("target", Profile.class);
    private static final CloudKey<Ticket> TICKET_KEY = CloudKey.cloudKey("ticket", Ticket.class);

    private final ProfileDescriptor profileDescriptor;
    private final TicketParser ticketParser;
    private final Set<TicketStatus> statuses;

    @AssistedInject
    public TargetTicketDescriptor(
        final ProfileDescriptor profileDescriptor,
        final TicketParser ticketParser,
        final @Assisted("statuses") Set<TicketStatus> statuses
    ) {
        this.profileDescriptor = profileDescriptor;
        this.ticketParser = ticketParser;
        this.statuses = statuses;
    }

    @Override
    public ArgumentParser<Commander, Ticket> parser() {
        return this;
    }

    @Override
    public List<CommandComponent<Commander>> components() {
        return List.of(
            CommandComponent.<Commander, Profile>builder()
                .key(TARGET_KEY)
                .parser(this.profileDescriptor.parser())
                .build(),
            CommandComponent.<Commander, Ticket>builder()
                .key(TICKET_KEY)
                .parser(this.ticketParser)
                .build()
        );
    }

    @Override
    public AggregateResultMapper.DirectAggregateResultMapper<Commander, Ticket> mapper() {
        return ($, context) -> ArgumentParseResult.success(context.get(TICKET_KEY));
    }

    @Override
    public TypeToken<Ticket> valueType() {
        return TypeToken.get(Ticket.class);
    }

}
