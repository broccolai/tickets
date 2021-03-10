package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class CommonCommands implements BaseCommand {

    protected final void processShow(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
//        Template[] templates = ticket.templates();
//
//        TextComponent.Builder show = Component.text()
//                .append(
//                        Message.SHOW__SENDER.use(templates),
//                        Message.SHOW__MESSAGE.use(templates),
//                        Message.SHOW__LOCATION.use(templates)
//                );
//
//        if (ticket.getStatus() != TicketStatus.PICKED) {
//            show.append(Message.SHOW__UNPICKED.use(templates));
//        } else {
//            show.append(Message.SHOW__PICKER.use(templates));
//        }
//
//        soul.sendMessage(show);
    }

    protected final void processLog(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
//        TextComponent.Builder component = Component.text()
//                .append(Message.TITLE__TICKET_LOG.use(ticket.templates()));
//
//        ticket.getMessages().forEach(message -> {
//            HoverEvent<Component> event;
//
//            if (message.getData() != null) {
//                event = HoverEvent.showText(
//                        Component.text(message.getData())
//                );
//            } else {
//                event = null;
//            }
//
//            component.append(Component.newline(), Message.FORMAT__LOG.use(message.templates()).hoverEvent(event));
//        });
//
//        soul.sendMessage(component);
    }

}
