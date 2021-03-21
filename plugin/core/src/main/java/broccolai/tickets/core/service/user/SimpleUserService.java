package broccolai.tickets.core.service.user;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.OfflineSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.user.UserService;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public abstract class SimpleUserService implements UserService {

    @Override
    public final @NonNull Soul wrap(final @NonNull UUID uuid) {

        if (uuid.equals(ConsoleSoul.UUID)) {
            return this.console();
        }

        if (this.isOnline(uuid)) {
            return this.player(uuid);
        }

        return new OfflineSoul(uuid);
    }

    @Override
    public final @NonNull Soul wrap(@NonNull final String name) {
        if (name.equalsIgnoreCase("CONSOLE")) {
            return this.console();
        }

        return this.wrap(this.uuidFromName(name));
    }

    protected abstract @NonNull UUID uuidFromName(@NonNull String name);

    protected abstract boolean isOnline(@NonNull UUID uuid);

}
