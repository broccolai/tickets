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
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.google.inject.Inject;

import com.google.inject.assistedinject.Assisted;

import java.util.ArrayList;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public final class TicketArgument extends CommandArgument<OnlineSoul, Ticket> {

    @Inject
    public TicketArgument(
            final @NonNull UserService userService,
            final @NonNull TicketService ticketService,
            final @Assisted("name") @NonNull String name
    ) {
        super(true, name, new TicketParser(userService, ticketService, ParserMode.TARGET), Ticket.class);
    }

    private static final class TicketParser implements ArgumentParser<OnlineSoul, Ticket> {

        private final UserService userService;
        private final TicketService ticketService;
        private final ParserMode parserMode;

        private TicketParser(
                final @NonNull UserService userService,
                final @NonNull TicketService ticketService,
                final @NonNull ParserMode parserMode
        ) {
            this.userService = userService;
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
            inputQueue.remove();

            if (value != null) {
                inputQueue.poll();
            } else {
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
                    return ArgumentParseResult.failure(new NoInputProvidedException(
                            TicketParser.class,
                            commandContext
                    ));
                }
            }

            Optional<Ticket> potentialTicket = this.ticketService.get(value);

            if (!potentialTicket.isPresent()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        TicketParser.class,
                        commandContext
                ));
            }

            Ticket ticket = potentialTicket.get();

            //todo: parser mode for this
//            if (!ticket.player().equals(target.uuid())) {
//                return ArgumentParseResult.failure(new NoInputProvidedException(
//                        TicketParser.class,
//                        commandContext
//                ));
//            }

            inputQueue.remove();
            return ArgumentParseResult.success(ticket);
        }

        @Override
        public @NonNull List<String> suggestions(
                final @NonNull CommandContext<OnlineSoul> commandContext,
                final @NonNull String input
        ) {
            int current = commandContext.getRawInput().size() - 3;

            if (current == 0) {
                return Lists.map(this.userService.players(), PlayerSoul::username);
            }

            String firstArgument = commandContext.getRawInput().get(2);
            Integer id = Numbers.valueOrNull(firstArgument, Integer::parseInt);

            if (id != null) {
                return new ArrayList<>();
            }

            Soul target = this.userService.wrap(firstArgument);

            return Lists.map(this.ticketService.get(target, EnumSet.allOf(TicketStatus.class)), ticket -> {
                return String.valueOf(ticket.id());
            });
        }

        @Override
        public int getRequestedArgumentCount() {
            return 2;
        }

    }

    enum ParserMode {
        TARGET
    }

}
