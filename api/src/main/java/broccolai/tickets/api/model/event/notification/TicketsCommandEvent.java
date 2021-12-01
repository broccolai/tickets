package broccolai.tickets.api.model.event.notification;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.TicketEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineUser;
import broccolai.tickets.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class TicketsCommandEvent implements TicketEvent, SoulEvent {

    private final NotificationReason notificationReason;
    protected final OnlineUser soul;
    protected final Ticket ticket;

    public TicketsCommandEvent(
            final @NonNull NotificationReason notificationReason,
            final @NonNull OnlineUser soul,
            final @NonNull Ticket ticket
    ) {
        this.notificationReason = notificationReason;
        this.soul = soul;
        this.ticket = ticket;
    }

    @Override
    public final @NonNull User soul() {
        return this.soul;
    }

    @Override
    public final @NonNull Ticket ticket() {
        return this.ticket;
    }

    //TODO
//    @Override
//    public final @NonNull JsonObject discord(final @NonNull UserService userService) {
//        JsonObject json = new JsonObject();
//
//        json.add("ticket", JsonUtility.ticket(userService, this.ticket));
//        json.add("author", JsonUtility.user(this.soul));
//        json.addProperty("action", this.notificationReason.name());
//
//        return json;
//    }

}
