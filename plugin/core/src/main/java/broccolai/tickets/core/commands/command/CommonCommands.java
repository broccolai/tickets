package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.MessageService;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class CommonCommands implements BaseCommand {

    private final MessageService messageService;

    public CommonCommands(
            final @NonNull MessageService messageService
    ) {
        this.messageService = messageService;
    }

    protected final void processShow(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        soul.sendMessage(this.messageService.showTicket(ticket));
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
