package love.broccolai.tickets.minecraft.common.parsers.ticket;

import java.util.Set;
import java.util.UUID;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.api.service.TicketSearch;
import love.broccolai.tickets.minecraft.common.exceptions.InvalidTicketException;
import love.broccolai.tickets.minecraft.common.exceptions.TicketNotFoundException;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TicketParser implements
    ArgumentParser<Commander, Ticket>,
    BlockingSuggestionProvider.Strings<Commander> {

    private static final int MINIMUM_ID = 0;
    private static final int MAXIMUM_ID = Integer.MAX_VALUE;

    private final StorageService storageService;
    private final boolean self;
    private final Set<TicketStatus> statuses;

    public TicketParser(
        final StorageService storageService,
        final boolean self,
        final Set<TicketStatus> statuses
    ) {
        this.storageService = storageService;
        this.self = self;
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
            .filter(ticket -> this.ticketMatchesTarget(commandContext, ticket))
            .filter(ticket -> this.statuses.contains(ticket.status()))
            .map(ArgumentParseResult::success)
            .orElse(ArgumentParseResult.failure(new TicketNotFoundException()));
    }

    @Override
    public Iterable<String> stringSuggestions(
        final CommandContext<Commander> commandContext,
        final CommandInput input
    ) {
        UUID targetUser = this.target(commandContext);

        TicketSearch search = TicketSearch.matching(this.statuses);

        if (targetUser != null) {
            search = search.createdBy(targetUser);
        }

        return Trove.of(this.storageService.findTickets(search))
            .map(Ticket::id)
            .map(String::valueOf)
            .toList();
    }

    private boolean ticketMatchesTarget(final CommandContext<Commander> commandContext, final Ticket ticket) {
        UUID targetUser = this.target(commandContext);

        return targetUser == null || ticket.creator().equals(targetUser);
    }

    private @Nullable UUID target(final CommandContext<Commander> commandContext) {
        if (this.self) {
            return commandContext.sender().uuid();
        }

        return null;
    }
}
