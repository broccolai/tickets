package broccolai.tickets.core.inject.module;

import broccolai.tickets.core.configuration.MainConfiguration;
import broccolai.tickets.core.inject.ForTickets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public final class ConfigurationModule extends AbstractModule {

    @Provides
    public MainConfiguration providesMainConfiguration(final @NonNull @ForTickets Path folder) throws IOException {
        Path file = folder.resolve("configuration.yml");

        if (!Files.exists(file)) {
            Files.createFile(file);
        }

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .defaultOptions(opts -> opts
                        .shouldCopyDefaults(true)
                        .header("For help setting up Discord Integration, look here "
                                + "https://github.com/broccolai/tickets/wiki/Discord-Integration "
                                + "or if you still need help join my Discord https://discord.broccol.ai")
                )
                .path(file)
                .build();
        CommentedConfigurationNode node = loader.load();
        MainConfiguration config = MainConfiguration.loadFrom(node);

        config.saveTo(node);
        loader.save(node);

        return config;
    }

}
