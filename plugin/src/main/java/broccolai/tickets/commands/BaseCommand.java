package broccolai.tickets.commands;

import broccolai.tickets.locale.Messages;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.user.Soul;
import broccolai.tickets.utilities.ReplacementUtilities;
import broccolai.tickets.utilities.TimeUtilities;
import broccolai.tickets.utilities.UserUtilities;
import cloud.commandframework.arguments.flags.FlagContext;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Base commands for ticket commands inherit from.
 */
public class BaseCommand {

    protected final void processShow(@NonNull final Soul soul, @NonNull final Ticket ticket) {
        String[] replacements = ReplacementUtilities.ticketReplacements(ticket);

        soul.message(Messages.TITLES__SHOW_TICKET, replacements);
        soul.message(Messages.SHOW__SENDER, replacements);
        soul.message(Messages.SHOW__MESSAGE, replacements);
        soul.message(Messages.SHOW__LOCATION, replacements);

        if (ticket.getStatus() != TicketStatus.PICKED) {
            soul.message(Messages.SHOW__UNPICKED);
        } else {
            soul.message(Messages.SHOW__PICKER, replacements);
        }
    }

    protected final void processLog(@NonNull final Soul soul, @NonNull final Ticket ticket) {
        String[] replacements = ReplacementUtilities.ticketReplacements(ticket);

        soul.message(Messages.TITLES__TICKET_LOG, replacements);

        ticket.getMessages().forEach(message -> {
            String suffix = message.getData() != null ? message.getData() : UserUtilities.nameFromUUID(message.getSender());

            soul.message(Messages.GENERAL__LOG_FORMAT, "reason", message.getReason().name(),
                    "date", TimeUtilities.formatted(message.getDate()), "suffix", suffix
            );
        });
    }

    protected final TicketStatus[] statusesFromFlags(final @NonNull FlagContext flags) {
        TicketStatus status = flags.getValue("status", null);

        return status != null ? arrayOf(status) : arrayOf(TicketStatus.OPEN, TicketStatus.CLOSED);
    }

    @SafeVarargs
    private final <T> T[] arrayOf(final @NonNull T... values) {
        return values;
    }
}
