package broccolai.tickets.core.service.message.moonshine;

import broccolai.tickets.api.model.ticket.Ticket;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.placeholder.IPlaceholderResolver;
import net.kyori.moonshine.util.Either;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

public final class TicketPlaceholderResolver implements IPlaceholderResolver<Audience, Ticket, Component> {

    @Override
    public @NonNull Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> resolve(
            final String placeholderName,
            final Ticket value,
            final Audience receiver,
            final Type owner,
            final Method method,
            final @Nullable Object[] parameters
    ) {
        return Map.of(
                placeholderName + "_id", Either.left(ConclusionValue.conclusionValue(Component.text(value.id()))),
                placeholderName + "_creator", Either.left(ConclusionValue.conclusionValue(Component.text(value.player().toString())))
        );
    }

}
