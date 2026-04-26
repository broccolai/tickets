package love.broccolai.tickets.common.service.storage;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.component.TicketAssignment;
import love.broccolai.tickets.api.model.component.TicketComponents;
import love.broccolai.tickets.api.model.component.TicketCreatedAt;
import love.broccolai.tickets.api.model.component.TicketCreator;
import love.broccolai.tickets.api.model.component.TicketStatusComponent;
import love.broccolai.tickets.api.model.component.TicketType;
import love.broccolai.tickets.common.utilities.QueriesLocator;
import org.jdbi.v3.core.Handle;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TicketQueryIndex {

    private final QueriesLocator locator;

    public TicketQueryIndex(final QueriesLocator locator) {
        this.locator = locator;
    }

    public void replace(
        final Handle handle,
        final int ticketId,
        final TicketComponents components,
        final Instant updatedAt
    ) {
        handle.createUpdate(this.locator.query("query-index/delete-ticket"))
            .bind("ticket", ticketId)
            .execute();

        handle.createUpdate(this.locator.query("query-index/insert-ticket"))
            .bind("ticket", ticketId)
            .bind("type_identifier", components.require(TicketType.KEY).format().identifier())
            .bind("status", components.require(TicketStatusComponent.KEY).status().name())
            .bindByType("creator", components.require(TicketCreator.KEY).creator(), UUID.class)
            .bindByType("assignee", this.assignee(components), UUID.class)
            .bindByType("created_at", components.require(TicketCreatedAt.KEY).date(), Instant.class)
            .bindByType("updated_at", updatedAt, Instant.class)
            .execute();
    }

    private @Nullable UUID assignee(final TicketComponents components) {
        return components.find(TicketAssignment.KEY)
            .map(TicketAssignment::assignee)
            .orElse(null);
    }
}
