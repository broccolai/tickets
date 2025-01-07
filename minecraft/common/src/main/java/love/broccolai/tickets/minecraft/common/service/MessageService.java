package love.broccolai.tickets.minecraft.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.api.model.Ticket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.moonshine.annotation.Message;
import net.kyori.moonshine.annotation.Placeholder;
import org.flywaydb.core.internal.util.StringUtils;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MessageService {

    @Message("feedback.ticket.header")
    Component ticketHeader(@Placeholder Ticket ticket);

    @Message("feedback.ticket.subheader")
    Component ticketSubheader(@Placeholder Ticket ticket);

    @Message("feedback.ticket.data")
    Component ticketDataString(@Placeholder String identifier, @Placeholder String content);

    @Message("feedback.ticket.data")
    Component ticketDataProfile(@Placeholder String identifier, @Placeholder UUID content);

    @Message("feedback.ticket.data")
    Component ticketDataLocation(@Placeholder String identifier, @Placeholder Location content);

    default Component ticketDisplay(final Ticket ticket) {
        List<ComponentLike> display = new ArrayList<>();

        display.add(Component.text());
        display.add(this.ticketHeader(ticket));
        display.add(this.ticketSubheader(ticket));
        display.add(Component.text());

        //todo: lol
        ticket.type().parts().forEach(part -> {
            Component data = switch (part.style()) {
                case Player -> this.ticketDataProfile(
                    StringUtils.capitalizeFirstLetter(part.identifier()),
                    (UUID) ticket.content().get(part.identifier()).second()
                );
                case Sentence -> this.ticketDataString(
                    StringUtils.capitalizeFirstLetter(part.identifier()),
                    (String) ticket.content().get(part.identifier()).second()
                );
                case Location -> this.ticketDataLocation(
                    StringUtils.capitalizeFirstLetter(part.identifier()),
                    (Location) ticket.content().get(part.identifier()).second()
                );
            };

            display.add(data);
            display.add(Component.text());
        });

        return Component.join(JoinConfiguration.newlines(), display);
    }
}
