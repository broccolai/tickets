package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.StatusTicketAction;
import love.broccolai.tickets.api.model.component.TicketComponents;
import love.broccolai.tickets.api.model.component.TicketCreatedAt;
import love.broccolai.tickets.api.model.component.TicketCreator;
import love.broccolai.tickets.api.model.component.TicketForm;
import love.broccolai.tickets.api.model.component.TicketStatusComponent;
import love.broccolai.tickets.api.model.format.TicketFormData;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketOpened(
    Instant date,
    UUID creator,
    TicketFormData form
) implements StatusTicketAction {

    public static final String TYPE = "tickets:opened";

    @Override
    public TicketStatus status() {
        return TicketStatus.OPEN;
    }

    @Override
    public TicketComponents apply(final TicketComponents components) {
        return components
            .with(new TicketCreator(this.creator))
            .with(new TicketCreatedAt(this.date))
            .with(new TicketForm(this.form))
            .with(new TicketStatusComponent(this.status()));
    }
}
