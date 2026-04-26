package love.broccolai.tickets.minecraft.common.parsers.ticket;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import io.leangen.geantyref.TypeToken;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.api.service.TicketSearch;
import love.broccolai.tickets.minecraft.common.exceptions.InvalidProfileException;
import love.broccolai.tickets.minecraft.common.exceptions.InvalidTicketException;
import love.broccolai.tickets.minecraft.common.exceptions.ProfileNotFoundException;
import love.broccolai.tickets.minecraft.common.exceptions.TicketNotFoundException;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.parser.aggregate.AggregateParser;
import org.incendo.cloud.parser.aggregate.AggregateResultMapper;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TargetTicketDescriptor implements AggregateParser<Commander, Ticket> {

    private static final CloudKey<Profile> TARGET_KEY = CloudKey.cloudKey("target", Profile.class);
    private static final CloudKey<Ticket> TICKET_KEY = CloudKey.cloudKey("ticket", Ticket.class);

    private final TargetProfileParser profileParser;
    private final TargetTicketParser ticketParser;

    @AssistedInject
    public TargetTicketDescriptor(
        final StorageService storageService,
        final ProfileService profileService,
        final @Assisted("statuses") Set<TicketStatus> statuses
    ) {
        this.profileParser = new TargetProfileParser(storageService, profileService, statuses);
        this.ticketParser = new TargetTicketParser(storageService, statuses);
    }

    @Override
    public List<CommandComponent<Commander>> components() {
        return List.of(
            CommandComponent.<Commander, Profile>builder()
                .key(TARGET_KEY)
                .parser(ParserDescriptor.of(this.profileParser, Profile.class))
                .build(),
            CommandComponent.<Commander, Ticket>builder()
                .key(TICKET_KEY)
                .parser(ParserDescriptor.of(this.ticketParser, Ticket.class))
                .build()
        );
    }

    @Override
    public AggregateResultMapper<Commander, Ticket> mapper() {
        return (commandContext, context) -> {
            Profile profile = context.get(TARGET_KEY);
            Ticket ticket = context.get(TICKET_KEY);

            if (!ticket.creator().equals(profile.uuid())) {
                return ArgumentParseResult.<Ticket>failure(new TicketNotFoundException()).asFuture();
            }

            return ArgumentParseResult.success(ticket).asFuture();
        };
    }

    @Override
    public TypeToken<Ticket> valueType() {
        return TypeToken.get(Ticket.class);
    }

    private static final class TargetProfileParser implements
        ArgumentParser<Commander, Profile>,
        BlockingSuggestionProvider.Strings<Commander> {

        private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");

        private final StorageService storageService;
        private final ProfileService profileService;
        private final Set<TicketStatus> statuses;

        private TargetProfileParser(
            final StorageService storageService,
            final ProfileService profileService,
            final Set<TicketStatus> statuses
        ) {
            this.storageService = storageService;
            this.profileService = profileService;
            this.statuses = statuses;
        }

        @Override
        public ArgumentParseResult<Profile> parse(
            final CommandContext<Commander> commandContext,
            final CommandInput commandInput
        ) {
            String input = commandInput.readString();

            if (!USERNAME_PATTERN.matcher(input).matches()) {
                return ArgumentParseResult.failure(new InvalidProfileException());
            }

            return this.profileService.find(input)
                .map(ArgumentParseResult::success)
                .orElse(ArgumentParseResult.failure(new ProfileNotFoundException()));
        }

        @Override
        public Iterable<String> stringSuggestions(
            final CommandContext<Commander> commandContext,
            final CommandInput input
        ) {
            Set<UUID> creators = this.storageService.findTickets(TicketSearch.matching(this.statuses))
                .stream()
                .map(Ticket::creator)
                .collect(Collectors.toCollection(LinkedHashSet::new));

            Map<UUID, Profile> profiles = this.profileService.load(creators);

            return creators.stream()
                .map(profiles::get)
                .filter(Objects::nonNull)
                .map(Profile::username)
                .toList();
        }
    }

    private static final class TargetTicketParser implements
        ArgumentParser<Commander, Ticket>,
        BlockingSuggestionProvider.Strings<Commander> {

        private static final int MINIMUM_ID = 0;
        private static final int MAXIMUM_ID = Integer.MAX_VALUE;

        private final StorageService storageService;
        private final Set<TicketStatus> statuses;

        private TargetTicketParser(
            final StorageService storageService,
            final Set<TicketStatus> statuses
        ) {
            this.storageService = storageService;
            this.statuses = statuses;
        }

        @Override
        public ArgumentParseResult<Ticket> parse(
            final CommandContext<Commander> commandContext,
            final CommandInput commandInput
        ) {
            if (!commandInput.isValidInteger(MINIMUM_ID, MAXIMUM_ID)) {
                return ArgumentParseResult.failure(new InvalidTicketException());
            }

            int ticketId = commandInput.readInteger();

            return this.storageService.selectTicket(ticketId)
                .filter(ticket -> this.statuses.contains(ticket.status()))
                .map(ArgumentParseResult::success)
                .orElse(ArgumentParseResult.failure(new TicketNotFoundException()));
        }

        @Override
        public Iterable<String> stringSuggestions(
            final CommandContext<Commander> commandContext,
            final CommandInput input
        ) {
            UUID target = commandContext.optional(TARGET_KEY)
                .map(Profile::uuid)
                .orElse(null);

            if (target == null) {
                return List.of();
            }

            return Trove.of(this.storageService.findTickets(TicketSearch.matching(this.statuses).createdBy(target)))
                .map(Ticket::id)
                .map(String::valueOf)
                .toList();
        }
    }
}
