package love.broccolai.tickets.api.service;

import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.AssociatedAction;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.format.TicketFormatContent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface StorageService {

    void addNotificationListener(PGNotificationListener listener);

    Ticket createTicket(UUID creator, TicketFormat type, TicketFormatContent content);

    void saveAction(Ticket ticket, Action action);

    Optional<Ticket> selectTicket(int id);

    Map<Integer, Ticket> selectTickets(int... ids);

    //todo: create search context?
    Collection<Ticket> findTickets(
        Set<TicketStatus> statuses,
        @Nullable UUID creator,
        @Nullable Instant since
    );

    AssociatedAction selectActionWithTicketReference(int id);
}
