package love.broccolai.tickets.minecraft.common.mooonshine.resolvers;

import com.google.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.ProfileService;
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

    private static final String UNKNOWN = "?";

    private final ProfileService profileService;

    @Inject
    public ProfilePlaceholderResolver(final ProfileService profileService) {
        this.profileService = profileService;
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
        String name = this.profileService.get(value.uuid())
            .map(Profile::username)
            .orElse(UNKNOWN);

        Component result = Component.text(name).hoverEvent(Component.text(value.uuid().toString()));

        return Either.left(conclusionValue(result));
    }

}
