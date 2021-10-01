package broccolai.tickets.core.service.message;

import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.message.moonshine.Causer;
import broccolai.tickets.api.service.message.moonshine.PermissionReceiver;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.core.utilities.ReflectionHelper;
import com.google.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.receiver.IReceiverLocator;
import net.kyori.moonshine.receiver.IReceiverLocatorResolver;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class PermissionReceiverResolver implements IReceiverLocatorResolver<Audience> {

    private final UserService userService;

    @Inject
    public PermissionReceiverResolver(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public @Nullable IReceiverLocator<Audience> resolve(
            final Method method, final Type proxy
    ) {
        final @Nullable PermissionReceiver annotation = method.getAnnotation(PermissionReceiver.class);

        if (annotation == null) {
            return null;
        }

        return new PermissionResolver(this.userService, annotation);
    }

    public static final class PermissionResolver implements IReceiverLocator<Audience> {

        private final UserService userService;
        private final PermissionReceiver annotation;

        public PermissionResolver(final UserService userService, final PermissionReceiver annotation) {
            this.userService = userService;
            this.annotation = annotation;
        }

        @Override
        public Audience locate(final Method method, final Object proxy, final @Nullable Object[] parameters) {
            final @Nullable Collection<Soul> causers = ReflectionHelper.parametersAnnotatedBy(
                    Causer.class,
                    method,
                    parameters
            );

            Collection<PlayerSoul> souls = this.userService.players();
            souls.removeIf(soul -> {
                return causers.contains(soul) || !soul.permission(this.annotation.permission());
            });
            return Audience.audience(souls);
        }

    }

}
