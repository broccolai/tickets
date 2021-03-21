package broccolai.tickets.core.commands.arguments;

import broccolai.corn.core.Lists;
import broccolai.corn.core.Numbers;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.core.exceptions.TicketNotFound;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.google.inject.Inject;

import com.google.inject.assistedinject.Assisted;

import java.util.ArrayList;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public final class TicketArgument extends CommandArgument<OnlineSoul, Ticket> {

    @Inject
    public TicketArgument(
            final @NonNull UserService userService,
            final @NonNull TicketService ticketService,
            final @Assisted("name") @NonNull String name,
            final @Assisted("mode") @NonNull TicketParserMode mode,
            final @Assisted("suggestions") @NonNull Set<TicketStatus> suggestions,
            final @Assisted("padding") int padding
    ) {
        super(true, name, new TicketParser(userService, ticketService, mode, suggestions, padding), Ticket.class);
    }

    public static final class TicketParser implements ArgumentParser<OnlineSoul, Ticket> {

        private final UserService userService;
        private final TicketService ticketService;
        private final TicketParserMode parserMode;
        private final Set<TicketStatus> suggestions;
        private final int padding;

        private TicketParser(
                final @NonNull UserService userService,
                final @NonNull TicketService ticketService,
                final @NonNull TicketParserMode parserMode,
                final @NonNull Set<TicketStatus> suggestions,
                final int padding
        ) {
            this.userService = userService;
            this.ticketService = ticketService;
            this.parserMode = parserMode;
            this.suggestions = suggestions;
            this.padding = padding;
        }

        @Override
        public @NonNull ArgumentParseResult<Ticket> parse(
                final @NonNull CommandContext<OnlineSoul> commandContext,
                final @NonNull Queue<String> inputQueue
        ) {
            switch (this.parserMode) {
                case ANY: return this.any(commandContext, inputQueue);
                case SENDERS: return this.senders(commandContext, inputQueue);
                default: throw new IllegalStateException();
            }
        }

        @Override
        public @NonNull List<String> suggestions(
                final @NonNull CommandContext<OnlineSoul> commandContext,
                final @NonNull String input
        ) {
            if (this.parserMode == TicketParserMode.SENDERS) {
                return this.ticketIds(commandContext.getSender());
            }

            int current = commandContext.getRawInput().size() - 3 - this.padding;

            if (current == 0) {
                return Lists.map(this.userService.players(), PlayerSoul::username);
            }

            String firstArgument = commandContext.getRawInput().get(2);
            Integer id = Numbers.valueOrNull(firstArgument, Integer::parseInt);

            if (id != null) {
                return new ArrayList<>();
            }

            Soul target = this.userService.wrap(firstArgument);

            return this.ticketIds(target);
        }

        private List<String> ticketIds(final @NonNull Soul target) {
            return Lists.map(this.ticketService.get(target, this.suggestions), ticket -> String.valueOf(ticket.id()));
        }

        @Override
        public int getRequestedArgumentCount() {
            return 2;
        }

        private ArgumentParseResult<Ticket> any(
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
            inputQueue.remove();

            if (value == null) {
                input = inputQueue.poll();

                if (input == null || input.isEmpty()) {
                    return ArgumentParseResult.failure(new NoInputProvidedException(
                            TicketParser.class,
                            commandContext
                    ));
                }

                try {
                    value = Integer.parseInt(input);
                } catch (Exception e) {
                    return ArgumentParseResult.failure(new TicketNotFound());
                }
            }

            Optional<Ticket> potentialTicket = this.ticketService.get(value);

            if (potentialTicket.isEmpty()) {
                return ArgumentParseResult.failure(new TicketNotFound());
            }

            Ticket ticket = potentialTicket.get();

            return ArgumentParseResult.success(ticket);
        }

        private ArgumentParseResult<Ticket> senders(
                final @NonNull CommandContext<OnlineSoul> commandContext,
                final @NonNull Queue<String> inputQueue
        ) {
            ArgumentParseResult<Ticket> result = this.any(commandContext, inputQueue);
            Optional<Ticket> potentialTicket = result.getParsedValue();

            if (potentialTicket.isEmpty()) {
                return result;
            }

            Ticket ticket = potentialTicket.get();

            if (!ticket.player().equals(commandContext.getSender().uuid())) {
                return ArgumentParseResult.failure(new TicketNotFound());
            }

            return result;
        }

    }

}
