package love.broccolai.tickets.minecraft.common.parsers;

import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.arguments.suggestion.Suggestion;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.context.CommandInput;
import cloud.commandframework.exceptions.parsing.NumberParseException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.minecraft.common.exceptions.TicketNotFoundException;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketIdentifierParser implements ArgumentParser<Commander, Ticket> {

    private static final int MINIMUM_ID = 0;
    private static final int MAXIMUM_ID = Integer.MAX_VALUE;

    private final StorageService storageService;
    private final ParserMode parserMode;
    private final Set<TicketStatus> statuses;

    public TicketIdentifierParser(
        final StorageService storageService,
        final ParserMode parserMode,
        final Set<TicketStatus> statuses
    ) {
        this.storageService = storageService;
        this.parserMode = parserMode;
        this.statuses = statuses;
    }

    @Override
    public ArgumentParseResult<Ticket> parse(
        final CommandContext<Commander> commandContext,
        final CommandInput commandInput
    ) {
        return this.parseNextInteger(
            commandContext,
            commandInput,
            ticket -> this.checkTicketValid(ticket, commandContext.getSender())
        );
    }

    private ArgumentParseResult<Ticket> parseNextInteger(
        final CommandContext<Commander> commandContext,
        final CommandInput commandInput,
        final Predicate<Ticket> predicate
    ) {
        if (!commandInput.isValidInteger(MINIMUM_ID, MAXIMUM_ID)) {
            return ArgumentParseResult.failure(new TicketIdentifierParseException(
                commandInput.peekString(),
                commandContext
            ));
        }

        int id = commandInput.readInteger();

        return this.storageService.selectTicket(id)
            .filter(predicate)
            .map(ArgumentParseResult::success)
            .orElse(ArgumentParseResult.failure(new TicketNotFoundException()));
    }

    @Override
    public List<Suggestion> suggestions(
        final CommandContext<Commander> commandContext,
        final String input
    ) {
        UUID source = commandContext
            .<Profile>getOptional("compound_target")
            .map(Profile::uuid)
            .orElse(commandContext.getSender().uuid());

        return Trove.of(this.storageService.findTickets(this.statuses, source, null))
            .map(Ticket::id)
            .map(String::valueOf)
            .map(Suggestion::simple)
            .toList();
    }

    private boolean checkTicketValid(final Ticket ticket, final Commander commander) {
        return switch (this.parserMode) {
            case ALL -> true;
            case SOURCED -> ticket.creator() == commander.uuid();
        };
    }

    public enum ParserMode {
        ALL,
        SOURCED
    }

    public static final class TicketIdentifierParseException extends NumberParseException {

        public TicketIdentifierParseException(String input, CommandContext<?> context) {
            super(input, 0, Integer.MAX_VALUE, TicketIdentifierParser.class, context);
        }

        @Override
        public String getNumberType() {
            return "integer";
        }

        @Override
        public boolean hasMin() {
            return true;
        }

        @Override
        public boolean hasMax() {
            return true;
        }
    }

}
