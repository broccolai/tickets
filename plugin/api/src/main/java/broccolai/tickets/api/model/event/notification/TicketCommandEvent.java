package broccolai.tickets.api.model.event.notification;

import broccolai.tickets.api.JsonUtility;
import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.TicketEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.user.UserService;
import com.google.gson.JsonObject;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class TicketCommandEvent implements TicketEvent, SoulEvent {

    private final NotificationReason notificationReason;
    protected final PlayerSoul soul;
    protected final Ticket ticket;

    public TicketCommandEvent(
            final @NonNull NotificationReason notificationReason,
            final @NonNull PlayerSoul soul,
            final @NonNull Ticket ticket
    ) {
        this.notificationReason = notificationReason;
        this.soul = soul;
        this.ticket = ticket;
    }

    @Override
    public final @NonNull PlayerSoul soul() {
        return this.soul;
    }

    @Override
    public final @NonNull Ticket ticket() {
        return this.ticket;
    }

    @Override
    public final @NonNull JsonObject discord(final @NonNull UserService userService) {
        JsonObject json = new JsonObject();

        json.add("ticket", JsonUtility.ticket(userService, this.ticket));
        json.add("author", JsonUtility.user(this.soul));
        json.addProperty("action", this.notificationReason.name());

        return json;
    }

}
