package love.broccolai.tickets.api.model;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.model.component.ComponentKey;
import love.broccolai.tickets.api.model.component.TicketAssignment;
import love.broccolai.tickets.api.model.component.TicketComponent;
import love.broccolai.tickets.api.model.component.TicketComponents;
import love.broccolai.tickets.api.model.component.TicketCreatedAt;
import love.broccolai.tickets.api.model.component.TicketCreator;
import love.broccolai.tickets.api.model.component.TicketForm;
import love.broccolai.tickets.api.model.component.TicketStatusComponent;
import love.broccolai.tickets.api.model.component.TicketType;
import love.broccolai.tickets.api.model.format.TicketFormData;
import love.broccolai.tickets.api.model.format.TicketFormat;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Ticket {

    int id();

    TicketComponents components();

    List<TicketAction> actions();

    Ticket withAction(TicketAction action);

    default <T extends TicketComponent> Optional<T> component(final ComponentKey<T> key) {
        return this.components().find(key);
    }

    default <T extends TicketComponent> T requireComponent(final ComponentKey<T> key) {
        return this.components().require(key);
    }

    default TicketFormat type() {
        return this.components().require(TicketType.KEY).format();
    }

    default UUID creator() {
        return this.components().require(TicketCreator.KEY).creator();
    }

    default Instant date() {
        return this.components().require(TicketCreatedAt.KEY).date();
    }

    default TicketStatus status() {
        return this.components().require(TicketStatusComponent.KEY).status();
    }

    default TicketFormData form() {
        return this.components().require(TicketForm.KEY).data();
    }

    default Optional<UUID> assignee() {
        return this.components().find(TicketAssignment.KEY)
            .map(TicketAssignment::assignee);
    }
}
