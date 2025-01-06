package love.broccolai.tickets.minecraft.common.parsers.ticket;

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
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TicketParser implements ArgumentParser<Commander, Ticket>, BlockingSuggestionProvider.Strings<Commander> {

    private static final int MINIMUM_ID = 0;
    private static final int MAXIMUM_ID = Integer.MAX_VALUE;

    private static final CloudKey<Profile> TARGET_KEY = CloudKey.cloudKey("target", Profile.class);

    private final StorageService storageService;
    private final boolean self;

    public TicketParser(final StorageService storageService, final boolean self) {
        this.storageService = storageService;
        this.self = self;
    }

    @Override
    public ArgumentParseResult<Ticket> parse(
        final CommandContext<Commander> commandContext,
        final CommandInput commandInput
    ) {
        if (!commandInput.isValidInteger(MINIMUM_ID, MAXIMUM_ID)) {
            return ArgumentParseResult.failure(new RuntimeException("cannot parse ticket"));
        }
        int id = commandInput.readInteger();

        return this.storageService.selectTicket(id)
            .filter(ticket -> this.ticketMatchesTarget(commandContext, ticket))
            .map(ArgumentParseResult::success)
            .orElse(ArgumentParseResult.failure(new TicketNotFoundException()));
    }

    @Override
    public Iterable<String> stringSuggestions(
        final CommandContext<Commander> commandContext,
        final CommandInput input
    ) {
        UUID source = this.target(commandContext);

        if (source == null) {
            return Collections.emptyList();
        }

        return Trove.of(this.storageService.findTickets(EnumSet.allOf(TicketStatus.class), source, null))
            .map(Ticket::id)
            .map(String::valueOf)
            .toList();
    }

    private boolean ticketMatchesTarget(final CommandContext<Commander> commandContext, final Ticket ticket) {
        UUID target = this.target(commandContext);

        return target == null || ticket.creator().equals(target);
    }

    private @Nullable UUID target(final CommandContext<Commander> commandContext) {
        if (this.self) {
            return commandContext.sender().uuid();
        }

        return commandContext.optional(TARGET_KEY)
            .map(Profile::uuid)
            .orElse(null);
    }
}
