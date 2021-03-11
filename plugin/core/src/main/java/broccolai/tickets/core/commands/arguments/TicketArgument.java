package broccolai.tickets.core.commands.arguments;

import broccolai.corn.core.Lists;
import broccolai.corn.core.Numbers;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.ticket.TicketService;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;

import cloud.commandframework.exceptions.parsing.NoInputProvidedException;

import com.google.inject.Inject;

import com.google.inject.assistedinject.Assisted;

import java.util.EnumSet;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Queue;

public final class TicketArgument extends CommandArgument<OnlineSoul, Ticket> {

    @Inject
    public TicketArgument(final @NonNull TicketService ticketService, final @Assisted("name") @NonNull String name) {
        super(true, name, new TicketParser(ticketService, ParserMode.TARGET), Ticket.class);
    }

    private static final class TicketParser implements ArgumentParser<OnlineSoul, Ticket> {

        private final TicketService ticketService;
        private final ParserMode parserMode;

        private TicketParser(final @NonNull TicketService ticketService, final @NonNull ParserMode parserMode) {
            this.ticketService = ticketService;
            this.parserMode = parserMode;
        }

        @Override
        public @NonNull ArgumentParseResult<Ticket> parse(
                final @NonNull CommandContext<OnlineSoul> commandContext,
                final @NonNull Queue<String> inputQueue
        ) {
            String input = inputQueue.peek();

            if (input == null || input.isEmpty()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        TicketParser.class,
                        commandContext
                ));
            }

            Integer value = Numbers.valueOrNull(input, Integer::parseInt);

            Soul target = commandContext.get("target");
            Optional<Ticket> potentialTicket = this.ticketService.get(value);

            if (!potentialTicket.isPresent()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        TicketParser.class,
                        commandContext
                ));
            }

            Ticket ticket = potentialTicket.get();

            if (!ticket.player().equals(target.uuid())) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        TicketParser.class,
                        commandContext
                ));
            }

            inputQueue.remove();
            return ArgumentParseResult.success(ticket);
        }

        @Override
        public @NonNull List<String> suggestions(
                final @NonNull CommandContext<OnlineSoul> commandContext,
                final @NonNull String input
        ) {
            Soul target = commandContext.get("target");
            return Lists.map(this.ticketService.get(target, EnumSet.allOf(TicketStatus.class)), ticket -> {
                return String.valueOf(ticket.id());
            });
        }

    }

    enum ParserMode {
        TARGET
    }

}
