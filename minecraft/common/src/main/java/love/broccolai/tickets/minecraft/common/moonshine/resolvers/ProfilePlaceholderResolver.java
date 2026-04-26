package love.broccolai.tickets.minecraft.common.moonshine.resolvers;

import com.google.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import love.broccolai.tickets.api.model.profile.Profile;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static net.kyori.moonshine.placeholder.ConclusionValue.conclusionValue;

@NullMarked
public final class ProfilePlaceholderResolver implements SinglePlaceholderResolver<Profile> {

    @Inject
    public ProfilePlaceholderResolver() {
    }

    @Override
    public Either<ConclusionValue<? extends Component>, ContinuanceValue<?>> single(
        final String placeholderName,
        final Profile value,
        final Audience receiver,
        final Type owner,
        final Method method,
        final @Nullable Object[] parameters
    ) {
        Component result = Component.text(value.username()).hoverEvent(Component.text(value.uuid().toString()));

        return Either.left(conclusionValue(result));
    }
}
