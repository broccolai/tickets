package broccolai.tickets.core.commands.command;

import broccolai.tickets.core.locale.Message;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.ticket.TicketStatus;
import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.UserManager;
import cloud.commandframework.arguments.flags.FlagContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.Template;
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
        Template[] templates = ticket.templates();

        TextComponent.Builder show = Component.text()
                .append(
                        Message.SHOW__SENDER.use(templates),
                        Component.newline(),
                        Message.SHOW__MESSAGE.use(templates),
                        Component.newline(),
                        Message.SHOW__LOCATION.use(templates),
                        Component.newline()
                );

        if (ticket.getStatus() != TicketStatus.PICKED) {
            show.append(Message.SHOW__UNPICKED.use(templates));
        } else {
            show.append(Message.SHOW__PICKER.use(templates));
        }

        soul.sendMessage(show);
    }

    protected final void processLog(final @NonNull Soul<C> soul, final @NonNull Ticket ticket) {
        TextComponent.Builder component = Component.text()
                .append(Message.TITLE__TICKET_LOG.use(ticket.templates()));

        ticket.getMessages().forEach(message -> {
            HoverEvent<Component> event;

            if (message.getData() != null) {
                event = HoverEvent.showText(
                        Component.text(message.getData())
                );
            } else {
                event = null;
            }

            component.append(Component.newline(), Message.FORMAT__LOG.use(message.templates()).hoverEvent(event));
        });

        soul.sendMessage(component);
    }

    protected final @NonNull TicketStatus[] statusesFromFlags(final @NonNull FlagContext flags) {
        TicketStatus status = flags.getValue("status", null);

        return status != null ? this.of(status) : this.of(TicketStatus.OPEN, TicketStatus.PICKED);
    }

    private @NonNull TicketStatus[] of(final @NonNull TicketStatus... values) {
        return values;
    }

}
