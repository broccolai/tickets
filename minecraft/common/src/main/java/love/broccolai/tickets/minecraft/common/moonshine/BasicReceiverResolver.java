package love.broccolai.tickets.minecraft.common.moonshine;

import com.google.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.UUID;
import love.broccolai.tickets.minecraft.common.moonshine.annotations.Causer;
import love.broccolai.tickets.minecraft.common.moonshine.annotations.PermissionReceiver;
import love.broccolai.tickets.minecraft.common.moonshine.annotations.Receiver;
import love.broccolai.tickets.minecraft.common.service.PermissionAudienceProvider;
import love.broccolai.tickets.minecraft.common.utilities.ReflectionHelper;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.exception.ReceiverMissingException;
import net.kyori.moonshine.receiver.IReceiverLocator;
import net.kyori.moonshine.receiver.IReceiverLocatorResolver;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class BasicReceiverResolver implements IReceiverLocatorResolver<Audience> {

    private final PermissionAudienceProvider permissionAudienceProvider;

    @Inject
    public BasicReceiverResolver(final PermissionAudienceProvider permissionAudienceProvider) {
        this.permissionAudienceProvider = permissionAudienceProvider;
    }

    @Override
    public IReceiverLocator<Audience> resolve(
        final Method method,
        final Type proxy
    ) {
        return new BasicResolver(this.permissionAudienceProvider);
    }

    public static final class BasicResolver implements IReceiverLocator<Audience> {

        private final PermissionAudienceProvider permissionAudienceProvider;

        public BasicResolver(final PermissionAudienceProvider permissionAudienceProvider) {
            this.permissionAudienceProvider = permissionAudienceProvider;
        }

        @Override
        public @Nullable Audience locate(
            final Method method,
            final Object proxy,
            final @Nullable Object[] parameters
        ) throws ReceiverMissingException {
            PermissionReceiver permissionReceiver = method.getAnnotation(PermissionReceiver.class);

            if (permissionReceiver != null) {
                UUID causer = ReflectionHelper.parameterAnnotatedBy(Causer.class, method, parameters);

                return this.permissionAudienceProvider.audience(permissionReceiver.permission(), causer);
            }

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
