package broccolai.tickets.core.service.impl;

import broccolai.tickets.core.model.user.ConsoleUserAudience;
import broccolai.tickets.core.model.user.UserAudience;
import broccolai.tickets.core.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.kyori.adventure.platform.AudienceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class SimpleUserService<S, P extends S> implements UserService<S, P> {

    private final Map<UUID, UserAudience> cache = new HashMap<>();

    protected final AudienceProvider audienceProvider;

    public SimpleUserService(final @NonNull AudienceProvider audienceProvider) {
        this.audienceProvider = audienceProvider;

        ConsoleUserAudience console = new ConsoleUserAudience(this.audienceProvider.console());
        this.cache.put(ConsoleUserAudience.UUID, console);
    }

    @Override
    public final @NonNull UserAudience wrap(final @NonNull S sender) {
        UUID uuid = this.uuid(sender);

        if (this.cache.containsKey(uuid)) {
            return this.cache.get(uuid);
        }

        return this.playerAudience(this.player(sender));
    }

}
