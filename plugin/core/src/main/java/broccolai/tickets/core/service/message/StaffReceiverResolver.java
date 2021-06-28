package broccolai.tickets.core.service.message;

import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.receiver.IReceiverLocator;
import net.kyori.moonshine.receiver.IReceiverLocatorResolver;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;

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
            //todo: Create UserService#players method with predicate to prevent unnecessary wrapping
            Collection<PlayerSoul> souls = this.userService.players();
            souls.removeIf(soul -> {
                return !soul.permission("tickets.staff.announce");
            });
            return Audience.audience(souls);
        }

    }

}
