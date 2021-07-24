package broccolai.tickets.core.service.message;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.message.moonshine.Receiver;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.core.utilities.ReflectionHelper;
import com.google.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.exception.ReceiverMissingException;
import net.kyori.moonshine.receiver.IReceiverLocator;
import net.kyori.moonshine.receiver.IReceiverLocatorResolver;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class BasicReceiverResolver implements IReceiverLocatorResolver<Audience> {

    private final UserService userService;

    @Inject
    public BasicReceiverResolver(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public IReceiverLocator<Audience> resolve(
            final Method method, final Type proxy
    ) {
        return new BasicResolver(this.userService);
    }

    public static final class BasicResolver implements IReceiverLocator<Audience> {

        private final UserService userService;

        public BasicResolver(final UserService userService) {
            this.userService = userService;
        }

        @Override
        public @Nullable Audience locate(
                final Method method,
                final Object proxy,
                final @Nullable Object[] parameters
        ) throws ReceiverMissingException {
            Object presentValue = ReflectionHelper.parameterAnnotatedBy(Receiver.class, method, parameters);

            if (presentValue == null) {
                return null;
            }

            if (presentValue instanceof Audience audience) {
                return audience;
            }

            if (presentValue instanceof Ticket ticket) {
                Soul soul = this.userService.wrap(ticket.player());

                if (soul instanceof OnlineSoul playerSoul) {
                    return playerSoul;
                }

                return Audience.empty();
            }

            throw new ReceiverMissingException("No annotated receiver") {
            };
        }

    }

}
