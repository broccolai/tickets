package love.broccolai.tickets.minecraft.common.moonshine.resolvers;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
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

import static net.kyori.moonshine.placeholder.ConclusionValue.conclusionValue;

@NullMarked
public final class LocationPlaceholderResolver implements SinglePlaceholderResolver<Location> {

    private static final JoinConfiguration JOINER = JoinConfiguration.builder()
        .separator(Component.text(", ", NamedTextColor.GRAY))
        .prefix(Component.text("[", NamedTextColor.GRAY))
        .suffix(Component.text("]", NamedTextColor.GRAY))
        .build();

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
            JOINER,
            Component.text(this.coordinate(value.x())),
            Component.text(this.coordinate(value.y())),
            Component.text(this.coordinate(value.z()))
        );

        return Either.left(conclusionValue(result));
    }

    private String coordinate(final double value) {
        return new DecimalFormat("#.##").format(value);
    }
}
