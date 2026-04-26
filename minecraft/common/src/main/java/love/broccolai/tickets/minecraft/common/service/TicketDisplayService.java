package love.broccolai.tickets.minecraft.common.service;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.ActionRegistry;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.model.component.ComponentKey;
import love.broccolai.tickets.api.model.component.ComponentRegistry;
import love.broccolai.tickets.api.model.component.TicketAssignment;
import love.broccolai.tickets.api.model.component.TicketComponent;
import love.broccolai.tickets.api.model.component.TicketCreatedAt;
import love.broccolai.tickets.api.model.component.TicketCreator;
import love.broccolai.tickets.api.model.component.TicketForm;
import love.broccolai.tickets.api.model.component.TicketStatusComponent;
import love.broccolai.tickets.api.model.component.TicketType;
import love.broccolai.tickets.api.model.format.TicketFormatPart;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketDisplayService {

    private static final Set<ComponentKey<?>> HEADER_COMPONENTS = Set.of(
        TicketType.KEY,
        TicketCreator.KEY,
        TicketCreatedAt.KEY,
        TicketStatusComponent.KEY,
        TicketAssignment.KEY
    );

    private static final String MISSING_FIELD = "Missing";

    private final MessageService messages;
    private final ComponentRegistry componentRegistry;
    private final ActionRegistry actionRegistry;

    @Inject
    public TicketDisplayService(
        final MessageService messages,
        final ComponentRegistry componentRegistry,
        final ActionRegistry actionRegistry
    ) {
        this.messages = messages;
        this.componentRegistry = componentRegistry;
        this.actionRegistry = actionRegistry;
    }

    public Component display(final Ticket ticket) {
        List<ComponentLike> display = new ArrayList<>();

        display.add(Component.text());
        display.add(this.messages.ticketHeader(ticket));
        display.add(this.messages.ticketSubheader(ticket));

        if (ticket.assignee().isPresent()) {
            display.add(this.messages.ticketStatusClaimed(ticket));
        } else {
            display.add(this.messages.ticketStatusUnclaimed(ticket));
        }

        display.add(Component.text());
        display.addAll(this.components(ticket));
        display.addAll(this.timeline(ticket));

        return Component.join(JoinConfiguration.newlines(), display);
    }

    private List<ComponentLike> components(final Ticket ticket) {
        List<ComponentLike> display = new ArrayList<>();

        for (TicketComponent component : ticket.components().values().values()) {
            if (HEADER_COMPONENTS.contains(component.key())) {
                continue;
            }

            if (component instanceof TicketForm form) {
                display.addAll(this.form(ticket, form));
            } else {
                display.addAll(this.fields(this.componentRegistry.encode(component)));
            }
        }

        return display;
    }

    private List<ComponentLike> form(final Ticket ticket, final TicketForm form) {
        List<ComponentLike> display = new ArrayList<>();

        for (TicketFormatPart part : ticket.type().parts()) {
            TicketDataField field = new TicketDataField(
                part.identifier(),
                TicketLabels.title(part.identifier()),
                form.data().parts().getOrDefault(part.identifier(), MISSING_FIELD)
            );

            display.add(this.messages.ticketData(field));
            display.add(Component.text());
        }

        return display;
    }

    private List<ComponentLike> fields(final Map<String, Object> payload) {
        List<ComponentLike> display = new ArrayList<>();

        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            TicketDataField field = new TicketDataField(
                entry.getKey(),
                TicketLabels.title(entry.getKey()),
                entry.getValue()
            );

            display.add(this.messages.ticketData(field));
            display.add(Component.text());
        }

        return display;
    }

    private List<ComponentLike> timeline(final Ticket ticket) {
        if (ticket.actions().isEmpty()) {
            return List.of();
        }

        List<ComponentLike> display = new ArrayList<>();

        display.add(this.messages.ticketTimelineHeader());

        for (TicketAction action : ticket.actions()) {
            String type = this.actionRegistry.type(action);

            display.add(this.messages.ticketTimelineEntry(
                TicketLabels.title(type),
                action.date(),
                action.creator()
            ));

            for (Map.Entry<String, Object> entry : this.actionRegistry.encode(action).entrySet()) {
                TicketDataField field = new TicketDataField(
                    entry.getKey(),
                    TicketLabels.title(entry.getKey()),
                    entry.getValue()
                );

                display.add(this.messages.ticketData(field));
            }

            display.add(Component.text());
        }

        return display;
    }
}
