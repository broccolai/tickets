package love.broccolai.tickets.minecraft.common.arguments;

import cloud.commandframework.arguments.compound.CompoundArgument;
import cloud.commandframework.types.tuples.Pair;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.leangen.geantyref.TypeToken;
import java.util.Set;
import java.util.function.Function;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.parsers.TicketIdentifierParser;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ProfileTicketArgument extends CompoundArgument<Pair<Profile, Ticket>, Commander, Ticket> {

    private static final Pair<String, String> ARGUMENT_NAMES = Pair.of("compound_target", "compound_id");
    private static final Pair<Class<Profile>, Class<Ticket>> ARGUMENT_TYPES = Pair.of(Profile.class, Ticket.class);

    private static final Function<Object[], Pair<Profile, Ticket>> ARRAY_UNWRAPPER = array -> Pair.of((Profile) array[0], (Ticket) array[1]);

    @Inject
    public ProfileTicketArgument(
        final StorageService storageService,
        final ProfileService profileService,
        final @Assisted("name") String name,
        final @Assisted("statuses") Set<TicketStatus> statuses
    ) {
        super(
            name,
            ARGUMENT_NAMES,
            Pair.of(
                new ProfileArgument.ProfileParser(profileService),
                new TicketIdentifierParser(storageService, TicketIdentifierParser.ParserMode.ALL, statuses)
            ),
            ARGUMENT_TYPES,
            ($, result) -> result.getSecond(),
            ARRAY_UNWRAPPER,
            TypeToken.get(Ticket.class)
        );
    }

}
