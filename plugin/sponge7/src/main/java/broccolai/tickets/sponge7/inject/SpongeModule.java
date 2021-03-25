package broccolai.tickets.sponge7.inject;

import broccolai.tickets.core.inject.platform.PluginPlatform;
import com.google.inject.AbstractModule;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.spongeapi.SpongeAudiences;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.Sponge;

public final class SpongeModule extends AbstractModule {

    private final PluginPlatform platform;

    public SpongeModule(final @NonNull PluginPlatform platform) {
        this.platform = platform;
    }

    @Override
    protected void configure() {
        this.bind(PluginPlatform.class).toInstance(this.platform);
        this.bind(AudienceProvider.class).toInstance(SpongeAudiences.create(
                Sponge.getPluginManager().getPlugin("PureTickets").get(),
                Sponge.getGame()
        ));
    }

}
