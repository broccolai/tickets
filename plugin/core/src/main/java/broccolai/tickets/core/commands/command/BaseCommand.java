package broccolai.tickets.core.commands.command;

import broccolai.tickets.core.locale.Messages;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.ticket.TicketStatus;
import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.UserManager;
import broccolai.tickets.core.utilities.ReplacementUtilities;
import broccolai.tickets.core.utilities.TimeUtilities;
import cloud.commandframework.arguments.flags.FlagContext;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class BaseCommand<C> {

    protected final UserManager<C, ?, ?> userManager;

    /**
     * Construct the base command
     *
     * @param userManager User manager
     */
    public BaseCommand(final @NonNull UserManager<C, ?, ?> userManager) {
        this.userManager = userManager;
    }

    protected final void processShow(final @NonNull Soul<C> soul, final @NonNull Ticket ticket) {
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

    protected final void processLog(final @NonNull Soul<C> soul, final @NonNull Ticket ticket) {
        String[] replacements = ReplacementUtilities.ticketReplacements(ticket);

        soul.message(Messages.TITLES__TICKET_LOG, replacements);

        ticket.getMessages().forEach(message -> {
            String suffix = message.getData() != null ? message.getData()
                    : message.getSender() != null ? this.userManager.getName(message.getSender()) : "";

            soul.message(Messages.GENERAL__LOG_FORMAT, "reason", message.getReason().name(),
                    "date", TimeUtilities.formatted(message.getDate()), "suffix", suffix
            );
        });
    }

    protected final @NonNull TicketStatus[] statusesFromFlags(final @NonNull FlagContext flags) {
        TicketStatus status = flags.getValue("status", null);

        return status != null ? arrayOf(status) : arrayOf(TicketStatus.OPEN, TicketStatus.CLOSED);
    }

    @SafeVarargs
    private final <T> @NonNull T[] arrayOf(final @NonNull T... values) {
        return values;
    }

}
