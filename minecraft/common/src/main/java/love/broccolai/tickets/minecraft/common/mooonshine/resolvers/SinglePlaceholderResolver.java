package love.broccolai.tickets.minecraft.common.mooonshine.resolvers;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.placeholder.IPlaceholderResolver;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface SinglePlaceholderResolver<T> extends IPlaceholderResolver<Audience, T, Component> {

    @Override
    default Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> resolve(
        final String placeholderName,
        final T value,
        final Audience receiver,
        final Type owner,
        final Method method,
        final @Nullable Object[] parameters
    ) {
        Either<ConclusionValue<? extends Component>, ContinuanceValue<?>> resolvedValue = this.single(
            placeholderName,
            value,
            receiver,
            owner,
            method,
            parameters
        );

        return Map.of(placeholderName, resolvedValue);
    }

    Either<ConclusionValue<? extends Component>, ContinuanceValue<?>> single(
        final String placeholderName,
        final T value,
        final Audience receiver,
        final Type owner,
        final Method method,
        final @Nullable Object[] parameters
    );

}
