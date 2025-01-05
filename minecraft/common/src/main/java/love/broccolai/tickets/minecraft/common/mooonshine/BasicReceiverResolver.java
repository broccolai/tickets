package love.broccolai.tickets.minecraft.common.mooonshine;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import love.broccolai.tickets.minecraft.common.mooonshine.annotations.Receiver;
import love.broccolai.tickets.minecraft.common.utilities.ReflectionHelper;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.exception.ReceiverMissingException;
import net.kyori.moonshine.receiver.IReceiverLocator;
import net.kyori.moonshine.receiver.IReceiverLocatorResolver;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class BasicReceiverResolver implements IReceiverLocatorResolver<Audience> {

    @Override
    public IReceiverLocator<Audience> resolve(
            final Method method,
            final Type proxy
    ) {
        return new BasicResolver();
    }

    public static final class BasicResolver implements IReceiverLocator<Audience> {

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

            throw new ReceiverMissingException("No annotated receiver") {};
        }

    }

}
