package broccolai.tickets.commands.arguments;

import broccolai.corn.core.Lists;
import broccolai.tickets.exceptions.TicketNotFound;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.user.Soul;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.context.CommandContext;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public final class TicketArgument extends CommandArgument<Soul, Ticket> {

    private TicketArgument(final boolean requiresId, final boolean issuer, @Nullable final TicketStatus... statuses) {
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
    public static TicketArgument of(final boolean requiresId, final boolean issuer, @Nullable final TicketStatus... statuses) {
        return new TicketArgument(requiresId, issuer, statuses);
    }


    private static final class TicketParser implements ArgumentParser<Soul, Ticket> {

        private final boolean requiresId;
        private final boolean issuer;
        @Nullable
        private final TicketStatus[] statuses;

        private TicketParser(final boolean requiresId, final boolean issuer, @Nullable final TicketStatus[] statuses) {
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
                    ticket = TicketSQL.select(inputId, target);
                } catch (NumberFormatException e) {
                    return ArgumentParseResult.failure(new IntegerArgument.IntegerParseException(
                            input,
                            0,
                            Integer.MAX_VALUE,
                            commandContext
                    ));
                }
            } else {
                ticket = TicketSQL.selectLastTicket(target, statuses);
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
            List<Integer> ids;

            try {
                if (issuer) {
                    ids = TicketSQL.selectIds(commandContext.getSender().getUniqueId(), statuses);
                } else {
                    OfflinePlayer target = commandContext.get("target");
                    ids = TicketSQL.selectIds(target.getUniqueId(), statuses);
                }
            } catch (final Exception e) {
                return new ArrayList<>();
            }

            return Lists.map(ids, Object::toString);
        }

    }

}
