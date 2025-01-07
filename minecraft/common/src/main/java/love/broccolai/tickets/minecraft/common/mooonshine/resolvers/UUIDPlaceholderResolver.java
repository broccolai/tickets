package love.broccolai.tickets.minecraft.common.mooonshine.resolvers;

import com.google.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.UUID;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static net.kyori.moonshine.placeholder.ContinuanceValue.continuanceValue;

@NullMarked
public final class UUIDPlaceholderResolver implements SinglePlaceholderResolver<UUID> {

    private final ProfileService profileService;

    @Inject
    public UUIDPlaceholderResolver(final ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public Either<ConclusionValue<? extends Component>, ContinuanceValue<?>> single(
        final String placeholderName,
        final UUID value,
        final Audience receiver,
        final Type owner,
        final Method method,
        final @Nullable Object[] parameters
    ) {
        Profile profile = this.profileService.get(value);
        return Either.right(continuanceValue(profile, Profile.class));
    }

}
