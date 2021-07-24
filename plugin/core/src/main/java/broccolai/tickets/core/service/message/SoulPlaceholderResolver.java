package broccolai.tickets.core.service.message;

import broccolai.tickets.api.model.user.Soul;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.placeholder.IPlaceholderResolver;
import net.kyori.moonshine.util.Either;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SoulPlaceholderResolver implements IPlaceholderResolver<Audience, Soul, Component> {

    @Override
    public @NonNull Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> resolve(
            final String placeholderName,
            final Soul soul,
            final Audience receiver,
            final Type owner,
            final Method method,
            final @Nullable Object[] parameters
    ) {
        return Map.of(
                placeholderName, Either.left(ConclusionValue.conclusionValue(Component.text(soul.username())))
        );
    }

}
