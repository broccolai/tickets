package love.broccolai.tickets.minecraft.common.moonshine.resolvers;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static net.kyori.moonshine.placeholder.ConclusionValue.conclusionValue;

@NullMarked
public final class InstantPlaceholderResolver implements SinglePlaceholderResolver<Instant> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("MMM d, yyyy HH:mm")
        .withZone(ZoneId.systemDefault());

    @Override
    public Either<ConclusionValue<? extends Component>, ContinuanceValue<?>> single(
        final String placeholderName,
        final Instant value,
        final Audience receiver,
        final Type owner,
        final Method method,
        final @Nullable Object[] parameters
    ) {
        Component display = Component.text(FORMATTER.format(value), NamedTextColor.GRAY);

        return Either.left(conclusionValue(display));
    }
}
