package love.broccolai.tickets.minecraft.common.mooonshine.resolvers;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.format.TicketFormat;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.placeholder.IPlaceholderResolver;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static net.kyori.moonshine.placeholder.ContinuanceValue.continuanceValue;

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
        TextColor statusColor = switch (value.status()) {
            case OPEN -> TextColor.color(0x00FF00);
            case PICKED -> TextColor.color(0xFFFF00);
            case CLOSED -> TextColor.color(0xFF0000);
        };

        Component id = Component.text("#" + value.id(), statusColor);

        return new PlaceholderMap(placeholderName)
            .conclusion("id", id)
            .continuance("creator", value.creator(), UUID.class)
            .continuance("format", value.type(), TicketFormat.class)
            .build();
    }
}
