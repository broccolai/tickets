package broccolai.tickets.core.service.user;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.user.UserService;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class SimpleUserService implements UserService {

    @Override
    public final @NonNull Soul wrap(final @NonNull UUID uuid) {

        if (uuid.equals(ConsoleSoul.UUID)) {
            return this.console();
        }

        if (this.isOnline(uuid)) {
            return this.player(uuid);
        }

        return this.offlinePlayer(uuid);
    }

    @Override
    public final @NonNull Soul wrap(final @NonNull String name) {
        if (name.equalsIgnoreCase("CONSOLE")) {
            return this.console();
        }

        return this.wrap(this.uuidFromName(name));
    }

    protected abstract @NonNull UUID uuidFromName(@NonNull String name);

    protected abstract boolean isOnline(@NonNull UUID uuid);

}
