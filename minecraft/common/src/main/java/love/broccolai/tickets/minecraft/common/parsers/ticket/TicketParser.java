package love.broccolai.tickets.minecraft.common.parsers.ticket;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.minecraft.common.exceptions.TicketNotFoundException;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.parsers.ProfileDescriptor;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketParser implements ArgumentParser<Commander, Ticket>, BlockingSuggestionProvider.Strings<Commander> {

    private static final int MINIMUM_ID = 0;
    private static final int MAXIMUM_ID = Integer.MAX_VALUE;

    private static final CloudKey<Profile> TARGET_KEY = CloudKey.cloudKey("target", Profile.class);

    private final StorageService storageService;

    @Inject
    public TicketParser(final StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public ArgumentParseResult<Ticket> parse(
        final CommandContext<Commander> commandContext,
        final CommandInput commandInput
    ) {
        if (!commandInput.isValidInteger(MINIMUM_ID, MAXIMUM_ID)) {
            return ArgumentParseResult.failure(new RuntimeException("cannot parse ticket"));
        }

        Optional<Profile> potentialProfile = commandContext.optional(ProfileDescriptor.LAST_FOUND_PROFILE);

        if (potentialProfile.isEmpty()) {
            return ArgumentParseResult.failure(new RuntimeException("no target"));
        }

        Profile profile = potentialProfile.get();
        int id = commandInput.readInteger();

        return this.storageService.selectTicket(id)
            .filter(ticket -> Objects.equal(ticket.creator(), profile.uuid()))
            .map(ArgumentParseResult::success)
            .orElse(ArgumentParseResult.failure(new TicketNotFoundException()));
    }

    @Override
    public Iterable<String> stringSuggestions(
        final CommandContext<Commander> commandContext,
        final CommandInput input
    ) {
        UUID source = commandContext
            .optional(TARGET_KEY)
            .map(Profile::uuid)
            .orElse(null);

        if (source == null) {
            return Collections.emptyList();
        }

        return Trove.of(this.storageService.findTickets(EnumSet.allOf(TicketStatus.class), source, null))
            .map(Ticket::id)
            .map(String::valueOf)
            .toList();
    }
}
