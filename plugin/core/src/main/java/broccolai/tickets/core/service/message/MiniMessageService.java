package broccolai.tickets.core.service.message;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.core.configuration.LocaleConfiguration;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public final class MiniMessageService implements MessageService {

    private final LocaleConfiguration locale;

    @Inject
    public MiniMessageService(final @NonNull LocaleConfiguration locale) {
        this.locale = locale;
    }

    @Override
    public Component senderTicketCreation(final @NonNull Ticket ticket) {
        return this.locale.sender.create.use(ticket);
    }

    @Override
    public Component commandsTicketList(final @NonNull Collection<@NonNull Ticket> tickets) {
        TextComponent.Builder builder = Component.text()
                .append(this.locale.title.yourTickets.use());

        tickets.forEach(ticket -> {
            Component list = this.locale.format.list.use(ticket);
            builder.append(Component.newline(), list);
        });

        return builder.build();
    }

    @Override
    public Component taskReminder(final int count) {
        return this.locale.format.reminder.use(Template.of("amount", String.valueOf(count)));
    }

}
