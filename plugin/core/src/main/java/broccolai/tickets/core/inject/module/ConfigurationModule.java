package broccolai.tickets.core.inject.module;

import broccolai.tickets.core.configuration.LocaleConfiguration;
import broccolai.tickets.core.configuration.MainConfiguration;
import broccolai.tickets.core.configuration.serializers.LocaleEntrySerializer;
import broccolai.tickets.core.model.locale.LocaleEntry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.io.File;

import java.io.IOException;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class ConfigurationModule extends AbstractModule {

    @Provides
    public MainConfiguration providesMainConfiguration(final @NonNull File folder) throws IOException {
        File file = new File(folder, "configuration.yml");
        file.createNewFile();

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .defaultOptions(opts -> opts.shouldCopyDefaults(true))
                .file(file)
                .build();
        CommentedConfigurationNode node = loader.load();
        MainConfiguration config = MainConfiguration.loadFrom(node);

        config.saveTo(node);
        loader.save(node);

        return config;
    }

    @Provides
    public LocaleConfiguration providesLocaleConfiguration(final @NonNull File folder) throws IOException {
        File file = new File(folder, "locale.yml");
        file.createNewFile();

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .defaultOptions(opts -> {
                    opts = opts.serializers(serializers -> {
                        serializers.register(LocaleEntry.class, LocaleEntrySerializer.INSTANCE);
                    });

                    return opts.shouldCopyDefaults(true);
                })
                .file(file)
                .build();
        CommentedConfigurationNode node = loader.load();
        LocaleConfiguration config = LocaleConfiguration.loadFrom(node);

        config.saveTo(node);
        loader.save(node);

        return config;
    }

}
