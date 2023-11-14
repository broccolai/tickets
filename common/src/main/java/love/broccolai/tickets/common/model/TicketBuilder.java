package love.broccolai.tickets.common.model;

import java.time.Instant;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketBuilder {

    private final int id;
    private final UUID creator;
    private final Instant date;
    private final SortedSet<Action> actions;

    public TicketBuilder(
        final int id,
        final UUID creator,
        final Instant date
    ) {
        this.id = id;
        this.creator = creator;
        this.date = date;
        this.actions = new TreeSet<>(Action.SORTER);
    }

    public TicketBuilder withAction(final Action action) {
        this.actions.add(action);
        return this;
    }

    public SimpleTicket build() {
        return new SimpleTicket(
            this.id,
            this.creator,
            this.date,
            this.actions
        );
    }

}
