package broccolai.tickets.core.service.impl;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.core.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.kyori.adventure.platform.AudienceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class SimpleUserService<S, P extends S> implements UserService<S, P> {

    private final Map<UUID, OnlineSoul> cache = new HashMap<>();

    protected final AudienceProvider audienceProvider;

    public SimpleUserService(final @NonNull AudienceProvider audienceProvider) {
        this.audienceProvider = audienceProvider;

        ConsoleSoul console = new ConsoleSoul(this.audienceProvider.console());
        this.cache.put(ConsoleSoul.UUID, console);
    }

    @Override
    public final @NonNull Soul wrap(@NonNull final UUID uuid) {
        return this.wrap(this.sender(uuid));
    }

    @Override
    @SuppressWarnings("unchecked")
    public final @NonNull OnlineSoul wrap(final @NonNull S sender) {
        UUID uuid = this.uuid(sender);

        if (this.cache.containsKey(uuid)) {
            return this.cache.get(uuid);
        }

        return this.player((P) sender);
    }

}
