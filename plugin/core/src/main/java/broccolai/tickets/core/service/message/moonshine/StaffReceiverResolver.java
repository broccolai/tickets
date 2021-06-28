package broccolai.tickets.core.service.message.moonshine;

import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.message.moonshine.Causer;
import broccolai.tickets.api.service.message.moonshine.StaffReceiver;
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

public final class StaffReceiverResolver implements IReceiverLocatorResolver<Audience> {

    private final UserService userService;

    @Inject
    public StaffReceiverResolver(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public @Nullable IReceiverLocator<Audience> resolve(
            final Method method, final Type proxy
    ) {
        final StaffReceiver annotation = method.getAnnotation(StaffReceiver.class);
        if (annotation == null) {
            return null;
        }

        return new StaffResolver(this.userService);
    }

    public static final class StaffResolver implements IReceiverLocator<Audience> {

        private final UserService userService;

        public StaffResolver(final UserService userService) {
            this.userService = userService;
        }

        @Override
        public Audience locate(final Method method, final Object proxy, final @Nullable Object[] parameters) {
            final Soul causer = ReflectionHelper.parameterAnnotatedBy(
                    Causer.class,
                    method,
                    parameters
            );

            Collection<PlayerSoul> souls = this.userService.players();
            souls.removeIf(soul -> {
                return soul.equals(causer) || !soul.permission("tickets.staff.announce");
            });
            return Audience.audience(souls);
        }

    }

}
