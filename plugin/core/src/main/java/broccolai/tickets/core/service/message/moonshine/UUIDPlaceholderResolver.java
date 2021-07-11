package broccolai.tickets.core.service.message.moonshine;

import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.placeholder.IPlaceholderResolver;
import net.kyori.moonshine.util.Either;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public final class UUIDPlaceholderResolver implements IPlaceholderResolver<Audience, UUID, Component> {

    private final UserService userService;

    @Inject
    public UUIDPlaceholderResolver(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public @NonNull Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> resolve(
            final String placeholderName,
            final UUID value,
            final Audience receiver,
            final Type owner,
            final Method method,
            final @Nullable Object[] parameters
    ) {
        return Map.of(
            placeholderName, Either.right(ContinuanceValue.continuanceValue(this.userService.snapshot(value), Soul.class))
        );
    }

}
