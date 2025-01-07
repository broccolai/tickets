package love.broccolai.tickets.minecraft.common.mooonshine.resolvers;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.format.TicketFormat;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.placeholder.IPlaceholderResolver;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TicketPlaceholderResolver implements IPlaceholderResolver<Audience, Ticket, Component> {

    @Override
    public Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> resolve(
        final String placeholderName,
        final Ticket value,
        final Audience receiver,
        final Type owner,
        final Method method,
        final @Nullable Object[] parameters
    ) {
        Component id = Component.text("#" + value.id())
            .color(TextColor.color(value.status().color()))
            .shadowColor(ShadowColor.shadowColor(NamedTextColor.BLACK, 130));

        PlaceholderMap placeholders = new PlaceholderMap(placeholderName)
            .conclusion("id", id)
            .continuance("creator", value.creator(), UUID.class)
            .continuance("format", value.type(), TicketFormat.class)
            .continuance("status", value.status(), TicketStatus.class);

        if (value.assignee().isPresent()) {
            placeholders.continuance("assignee", value.assignee().get(), UUID.class);
        } else {
            placeholders.conclusion("assignee", Component.text("NONE?"));
        }

        return placeholders.build();
    }
}
