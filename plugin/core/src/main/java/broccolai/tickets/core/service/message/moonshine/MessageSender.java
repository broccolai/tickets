package broccolai.tickets.core.service.message.moonshine;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.message.IMessageSender;

@Singleton
public final class MessageSender implements IMessageSender<Audience, Component> {

    @Override
    public void send(final Audience receiver, final Component renderedMessage) {
        receiver.sendMessage(renderedMessage);
    }

}
