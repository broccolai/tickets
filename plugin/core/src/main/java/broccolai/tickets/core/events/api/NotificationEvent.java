package broccolai.tickets.core.events.api;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.core.events.Event;
import broccolai.tickets.core.interactions.NotificationReason;
import broccolai.tickets.core.ticket.Ticket;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class NotificationEvent implements Event {

    private final NotificationReason reason;
    private final OnlineSoul sender;
    private final UUID target;
    private final Ticket involvedTicket;
    private final Template[] templates;

    /**
     * @param reason         Reason for notification
     * @param sender         Sender soul
     * @param target         Targets unique id
     * @param involvedTicket Ticket involved in the notification
     */
    public NotificationEvent(
            final NotificationReason reason,
            final @NonNull OnlineSoul sender,
            final @Nullable UUID target,
            final @NonNull Ticket involvedTicket
    ) {
        this.reason = reason;
        this.sender = sender;
        this.target = target;
        this.involvedTicket = involvedTicket;

        Template[] extraTemplates = new Template[1];
        extraTemplates[0] = Template.of("user", sender.username());

        this.templates = concat(extraTemplates, involvedTicket.templates());
    }

    /**
     * @return Notification reason
     */
    public NotificationReason getReason() {
        return reason;
    }

    /**
     * @return Senders soul
     */
    public OnlineSoul getSender() {
        return sender;
    }

    /**
     * @return Optional target unique id
     */
    public Optional<UUID> getTarget() {
        return Optional.ofNullable(target);
    }

    /**
     * @return Involved ticket
     */
    public Ticket getInvolvedTicket() {
        return involvedTicket;
    }

    /**
     * @return Array of templates
     */
    public Template[] getTemplates() {
        return templates;
    }

    private static <T> T[] concat(final @NonNull T[] first, final @NonNull T[] second) {
        T[] output = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, output, first.length, second.length);
        return output;
    }

}
