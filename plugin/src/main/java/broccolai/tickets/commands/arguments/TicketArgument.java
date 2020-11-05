package broccolai.tickets.commands.arguments;

import broccolai.tickets.exceptions.TicketNotFound;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketIdStorage;
import broccolai.tickets.ticket.TicketManager;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.user.Soul;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.context.CommandContext;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public final class TicketArgument extends CommandArgument<Soul, Ticket> {

    private TicketArgument(final boolean requiresId, final boolean issuer, @NonNull final TicketStatus... statuses) {
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
    public static TicketArgument of(final boolean requiresId, final boolean issuer, @NonNull final TicketStatus... statuses) {
        return new TicketArgument(requiresId, issuer, statuses);
    }


    private static final class TicketParser implements ArgumentParser<Soul, Ticket> {

        private final boolean requiresId;
        private final boolean issuer;
        private final @NonNull TicketStatus[] statuses;

        private TicketParser(final boolean requiresId, final boolean issuer, @NonNull final TicketStatus[] statuses) {
            this.requiresId = requiresId;
            this.issuer = issuer;
            this.statuses = statuses;
        }

        @NonNull
        @Override
        public ArgumentParseResult<Ticket> parse(
                @NonNull final CommandContext<Soul> commandContext,
                @NonNull final Queue<String> inputQueue
        ) {
            TicketManager ticketManager = commandContext.get("ticketManager");
            UUID target;

            if (issuer) {
                target = commandContext.getSender().getUniqueId();
            } else {
                target = commandContext.<OfflinePlayer>get("target").getUniqueId();
            }

            String input = inputQueue.peek();
            Ticket ticket;

            if (requiresId || input != null) {
                try {
                    @SuppressWarnings("ConstantConditions") final int inputId = Integer.parseInt(input);
                    ticket = ticketManager.getTicket(inputId).orElse(null);

                    if (ticket != null && ticket.getPlayerUUID() != target) {
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

        @NonNull
        @Override
        public List<String> suggestions(@NonNull final CommandContext<Soul> commandContext, @NonNull final String input) {
            TicketIdStorage idStorage = commandContext.<TicketManager>get("ticketManager").getIdStorage();

            try {
                UUID uuid;

                if (issuer) {
                    uuid = commandContext.getSender().getUniqueId();
                } else {
                    OfflinePlayer target = commandContext.get("target");
                    uuid = target.getUniqueId();
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
