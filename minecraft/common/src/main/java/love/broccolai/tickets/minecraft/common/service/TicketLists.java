package love.broccolai.tickets.minecraft.common.service;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import love.broccolai.tickets.api.model.Ticket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketLists {

    private final MessageService messages;

    @Inject
    public TicketLists(final MessageService messages) {
        this.messages = messages;
    }

    public Component user(final Collection<Ticket> tickets) {
        List<Component> message = new ArrayList<>();

        message.add(this.messages.feedbackUserListHeader());

        if (tickets.isEmpty()) {
            message.add(this.messages.feedbackUserListEmpty());
        } else {
            tickets.forEach(ticket -> message.add(this.messages.feedbackUserListEntry(ticket)));
        }

        return Component.join(JoinConfiguration.newlines(), message);
    }

    public Component staff(final Collection<Ticket> tickets) {
        List<Component> message = new ArrayList<>();

        message.add(this.messages.feedbackStaffListHeader());

        if (tickets.isEmpty()) {
            message.add(this.messages.feedbackStaffListEmpty());
        } else {
            this.grouped(tickets).forEach((type, group) -> {
                message.add(this.messages.feedbackStaffListGroup(type));

                group.forEach(ticket -> message.add(this.messages.feedbackStaffListEntry(ticket)));
                message.add(Component.empty());
            });
        }

        return Component.join(JoinConfiguration.newlines(), message);
    }

    private Map<String, List<Ticket>> grouped(final Collection<Ticket> tickets) {
        return tickets.stream()
            .collect(Collectors.groupingBy(
                ticket -> ticket.type().displayName(),
                LinkedHashMap::new,
                Collectors.toList()
            ));
    }
}
