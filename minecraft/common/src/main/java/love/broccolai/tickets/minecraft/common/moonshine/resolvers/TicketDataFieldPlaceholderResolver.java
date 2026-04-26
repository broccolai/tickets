package love.broccolai.tickets.minecraft.common.moonshine.resolvers;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.minecraft.common.service.TicketDataField;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.placeholder.IPlaceholderResolver;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TicketDataFieldPlaceholderResolver implements IPlaceholderResolver<Audience, TicketDataField, Component> {

    @Override
    public Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> resolve(
        final String placeholderName,
        final TicketDataField value,
        final Audience receiver,
        final Type owner,
        final Method method,
        final @Nullable Object[] parameters
    ) {
        return new PlaceholderMap()
            .conclusion("identifier", Component.text(value.label()))
            .continuance("content", this.content(value.value()), this.type(value.value()))
            .build();
    }

    private Object content(final Object value) {
        if (this.supported(value)) {
            return value;
        }

        return String.valueOf(value);
    }

    private Type type(final Object value) {
        if (value instanceof Profile) {
            return Profile.class;
        }

        if (value instanceof Location) {
            return Location.class;
        }

        if (value instanceof UUID) {
            return UUID.class;
        }

        if (value instanceof Instant) {
            return Instant.class;
        }

        if (value instanceof Number) {
            return Number.class;
        }

        return String.class;
    }

    private boolean supported(final Object value) {
        return value instanceof Profile
            || value instanceof Location
            || value instanceof UUID
            || value instanceof Instant
            || value instanceof Number
            || value instanceof String;
    }
}
