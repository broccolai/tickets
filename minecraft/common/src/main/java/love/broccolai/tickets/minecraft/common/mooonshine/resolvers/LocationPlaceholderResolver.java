package love.broccolai.tickets.minecraft.common.mooonshine.resolvers;

import love.broccolai.tickets.api.model.Location;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DecimalFormat;

import static net.kyori.moonshine.placeholder.ConclusionValue.conclusionValue;

@NullMarked
public final class LocationPlaceholderResolver implements SinglePlaceholderResolver<Location> {

    private static final JoinConfiguration joiner = JoinConfiguration.builder()
        .separator(Component.text(", ", NamedTextColor.GRAY))
        .prefix(Component.text("[", NamedTextColor.GRAY))
        .suffix(Component.text("]", NamedTextColor.GRAY))
        .build();
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    @Override
    public Either<ConclusionValue<? extends Component>, ContinuanceValue<?>> single(
        final String placeholderName,
        final Location value,
        final Audience receiver,
        final Type owner,
        final Method method,
        final @Nullable Object[] parameters
    ) {
        Component result = Component.join(
            joiner,
            Component.text(DECIMAL_FORMAT.format(value.x())),
            Component.text(DECIMAL_FORMAT.format(value.y())),
            Component.text(DECIMAL_FORMAT.format(value.z()))
        );

        return Either.left(conclusionValue(result));
    }

}
