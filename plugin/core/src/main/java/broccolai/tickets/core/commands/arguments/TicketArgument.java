package broccolai.tickets.core.commands.arguments;

import broccolai.tickets.core.exceptions.TicketNotFound;
import broccolai.tickets.core.model.user.UserAudience;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.ticket.TicketIdStorage;
import broccolai.tickets.core.ticket.TicketManager;
import broccolai.tickets.core.ticket.TicketStatus;
import broccolai.tickets.core.user.User;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.context.CommandContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public final class TicketArgument extends CommandArgument<UserAudience, Ticket> {

    private TicketArgument(final boolean requiresId, final boolean issuer, final @NonNull TicketStatus... statuses) {
        super(true, "ticket", new TicketParser(requiresId, issuer, statuses), Ticket.class);
    }

    /**
     * Create a new required command argument
     *
     * @param requiresId Should require an id
     * @param issuer     Parse from the sender
     * @param statuses   Applicable ticket statuses
     * @return Constructed ticket argument
     */
    public static @NonNull TicketArgument of(
            final boolean requiresId,
            final boolean issuer,
            final @NonNull TicketStatus... statuses
    ) {
        return new TicketArgument(requiresId, issuer, statuses);
    }


    private static final class TicketParser implements ArgumentParser<UserAudience, Ticket> {

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
                final @NonNull CommandContext<UserAudience> commandContext,
                final @NonNull Queue<String> inputQueue
        ) {
            TicketManager ticketManager = commandContext.get("ticketManager");
            UserAudience target;

            if (issuer) {
                target = commandContext.getSender();
            } else {
                target = commandContext.get("target");
            }

            String input = inputQueue.peek();
            Ticket ticket;

            if (requiresId || input != null) {
                try {
                    @SuppressWarnings("ConstantConditions") final int inputId = Integer.parseInt(input);
                    ticket = ticketManager.getTicket(inputId).orElse(null);

                    if (ticket != null && !ticket.getPlayer().equals(target)) {
                        ticket = null;
                    }
                } catch (NumberFormatException e) {
                    return ArgumentParseResult.failure(new IntegerArgument.IntegerParseException(
                            input,
                            0,
                            Integer.MAX_VALUE,
                            commandContext
                    ));
                }
            } else {
                ticket = ticketManager.getRecentTicket(target, statuses).orElse(null);
            }

            if (ticket == null) {
                return ArgumentParseResult.failure(new TicketNotFound());
            }

            inputQueue.remove();
            return ArgumentParseResult.success(ticket);
        }

        @Override
        public @NonNull List<String> suggestions(
                final @NonNull CommandContext<UserAudience> commandContext,
                final @NonNull String input
        ) {
            TicketIdStorage idStorage = commandContext.<TicketManager>get("ticketManager").getIdStorage();

            try {
                UUID uuid;

                if (issuer) {
                    uuid = commandContext.getSender().uuid();
                } else {
                    User user = commandContext.get("target");
                    uuid = user.getUniqueId();
                }

                List<String> ids = new ArrayList<>();

                for (final Integer id : idStorage.getIds(uuid, statuses)) {
                    ids.add(id.toString());
                }

                return ids;
            } catch (final Exception e) {
                return new ArrayList<>();
            }
        }

    }

}
