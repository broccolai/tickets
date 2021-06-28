package broccolai.tickets.core.service.message;

import broccolai.tickets.api.service.message.moonshine.Receiver;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.exception.ReceiverMissingException;
import net.kyori.moonshine.receiver.IReceiverLocator;
import net.kyori.moonshine.receiver.IReceiverLocatorResolver;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public final class BasicReceiverResolver implements IReceiverLocatorResolver<Audience> {

    @Override
    public IReceiverLocator<Audience> resolve(
            final Method method, final Type proxy
    ) {
        return new BasicResolver();
    }

    public static final class BasicResolver implements IReceiverLocator<Audience> {

        @Override
        public Audience locate(
                final Method method,
                final Object proxy,
                final @Nullable Object[] parameters
        ) throws ReceiverMissingException {
            Parameter[] reflectedParameters = method.getParameters();

            for (int i = 0; i < reflectedParameters.length; i++) {
                Parameter reflectedParameter = reflectedParameters[i];
                if (reflectedParameter.isAnnotationPresent(Receiver.class)) {
                    return (Audience) parameters[i];
                }
            }

            throw new ReceiverMissingException("receiver not found") {};
        }

    }

}
