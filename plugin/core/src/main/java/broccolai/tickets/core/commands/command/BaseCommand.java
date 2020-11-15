package broccolai.tickets.core.commands.command;

import broccolai.tickets.core.locale.Message;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.ticket.TicketStatus;
import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.UserManager;
import cloud.commandframework.arguments.flags.FlagContext;
import net.kyori.adventure.text.Component;
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
        Component show = Message.FORMAT__SHOW.use(ticket.templates());

        soul.sendMessage(show);
    }

    protected final void processLog(final @NonNull Soul<C> soul, final @NonNull Ticket ticket) {
        Component title = Message.TITLE__TICKET_LOG.use(ticket.templates());
        soul.sendMessage(title);

        ticket.getMessages().forEach(message -> {
            Component log = Message.FORMAT__LOG.use(message.templates());

            soul.sendMessage(log);
        });
    }

    protected final @NonNull TicketStatus[] statusesFromFlags(final @NonNull FlagContext flags) {
        TicketStatus status = flags.getValue("status", null);

        return status != null ? of(status) : of(TicketStatus.OPEN, TicketStatus.CLOSED);
    }

    private @NonNull TicketStatus[] of(final @NonNull TicketStatus... values) {
        return values;
    }

}
