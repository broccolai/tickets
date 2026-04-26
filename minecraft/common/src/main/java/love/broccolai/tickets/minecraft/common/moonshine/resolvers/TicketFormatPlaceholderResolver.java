package love.broccolai.tickets.minecraft.common.moonshine.resolvers;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import love.broccolai.tickets.api.model.format.TicketFormat;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static net.kyori.moonshine.placeholder.ConclusionValue.conclusionValue;

@NullMarked
public final class TicketFormatPlaceholderResolver implements SinglePlaceholderResolver<TicketFormat> {

    @Override
    public Either<ConclusionValue<? extends Component>, ContinuanceValue<?>> single(
        final String placeholderName,
        final TicketFormat value,
        final Audience receiver,
        final Type owner,
        final Method method,
        final @Nullable Object[] parameters
    ) {
        Component display = Component.text(value.displayName());

        return Either.left(conclusionValue(display));
    }
}
