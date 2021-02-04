package broccolai.tickets.core.service.impl;

import broccolai.tickets.core.locale.Message;
import broccolai.tickets.core.service.MessageService;
import broccolai.tickets.core.ticket.Ticket;

import java.util.Collection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public final class MiniMessageService implements MessageService {

    @Override
    public Component commandsTicketList(final Collection<Ticket> tickets) {
        TextComponent.Builder builder = Component.text()
                .append(Message.TITLE__YOUR_TICKETS.use());

        tickets.forEach(ticket -> {
            Component list = Message.FORMAT__LIST.use(ticket.templates());
            builder.append(Component.newline(), list);
        });

        return builder.build();
    }

}
