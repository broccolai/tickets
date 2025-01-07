package love.broccolai.tickets.minecraft.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.minecraft.common.mooonshine.annotations.Receiver;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.moonshine.annotation.Message;
import net.kyori.moonshine.annotation.Placeholder;
import org.flywaydb.core.internal.util.StringUtils;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MessageService {

    @Message("ticket.header")
    Component ticketHeader(@Placeholder Ticket ticket);

    @Message("ticket.subheader")
    Component ticketSubheader(@Placeholder Ticket ticket);

    @Message("ticket.status.unclaimed")
    Component ticketStatusUnclaimed(@Placeholder Ticket ticket);

    @Message("ticket.status.claimed")
    Component ticketStatusClaimed(@Placeholder Ticket ticket);

    @Message("ticket.data")
    Component ticketDataString(@Placeholder String identifier, @Placeholder String content);

    @Message("ticket.data")
    Component ticketDataProfile(@Placeholder String identifier, @Placeholder UUID content);

    @Message("ticket.data")
    Component ticketDataLocation(@Placeholder String identifier, @Placeholder Location content);

    @Message("feedback.user.create")
    void feedbackUserCreate(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.staff.list_header")
    Component feedbackStaffListHeader();

    @Message("feedback.staff.list_entry")
    Component feedbackStaffListEntry(@Placeholder Ticket ticket);

    default Component ticketDisplay(final Ticket ticket) {
        List<ComponentLike> display = new ArrayList<>();

        display.add(Component.text());
        display.add(this.ticketHeader(ticket));
        display.add(this.ticketSubheader(ticket));

        if (ticket.assignee().isPresent()) {
            display.add(this.ticketStatusClaimed(ticket));
        } else {
            display.add(this.ticketStatusUnclaimed(ticket));
        }
        display.add(Component.text());

        ticket.type().parts().forEach(part -> {
            String identifier = StringUtils.capitalizeFirstLetter(part.identifier());

            Component data = switch (part.style()) {
                case Player -> this.ticketDataProfile(
                    identifier,
                    ticket.content().get(part.identifier())
                );
                case Sentence -> this.ticketDataString(
                    identifier,
                    ticket.content().get(part.identifier())
                );
                case Location -> this.ticketDataLocation(
                    identifier,
                    ticket.content().get(part.identifier())
                );
            };

            display.add(data);
            display.add(Component.text());
        });

        return Component.join(JoinConfiguration.newlines(), display);
    }
}
