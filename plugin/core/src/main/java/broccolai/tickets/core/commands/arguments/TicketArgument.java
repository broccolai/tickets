package broccolai.tickets.core.commands.arguments;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.OnlineSoul;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Queue;

public final class TicketArgument extends CommandArgument<OnlineSoul, Ticket> {

    private TicketArgument(final boolean requiresId, final boolean issuer, final @NonNull TicketStatus... statuses) {
        super(true, "ticket", new TicketParser(requiresId, issuer, statuses), Ticket.class);
    }

    public static @NonNull TicketArgument of(
            final boolean requiresId,
            final boolean issuer,
            final @NonNull TicketStatus... statuses
    ) {
        return new TicketArgument(requiresId, issuer, statuses);
    }


    private static final class TicketParser implements ArgumentParser<OnlineSoul, Ticket> {

        private final boolean requiresId;
        private final boolean issuer;
        private final @NonNull TicketStatus[] statuses;

        private TicketParser(final boolean requiresId, final boolean issuer, final @NonNull TicketStatus[] statuses) {
            this.requiresId = requiresId;
            this.issuer = issuer;
            this.statuses = statuses;
        }

        @Override
        public @NonNull ArgumentParseResult<Ticket> parse(
                final @NonNull CommandContext<OnlineSoul> commandContext,
                final @NonNull Queue<String> inputQueue
        ) {
            //todo:
            return null;
        }

        @Override
        public @NonNull List<String> suggestions(
                final @NonNull CommandContext<OnlineSoul> commandContext,
                final @NonNull String input
        ) {
            //todo:
            return null;
        }

    }

}
