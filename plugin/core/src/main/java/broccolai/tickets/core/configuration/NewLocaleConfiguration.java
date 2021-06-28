package broccolai.tickets.core.configuration;

import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.inject.ForTickets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Singleton
public final class NewLocaleConfiguration {

    private final Map<String, String> entries;

    @Inject
    public NewLocaleConfiguration(final @ForTickets Path folder, final MainConfiguration configuration) throws ConfigurateException {
        Path localeFolder = folder.resolve("locales");
        Path file = localeFolder.resolve(this.makeLocaleFileName(configuration.locale));

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(file)
                .build();

        CommentedConfigurationNode node = loader.load();
        node = node.mergeFrom(this.loadDefault(configuration.locale));

        loader.save(node);
        this.entries = this.load(node);
    }

    public String get(final String key) {
        return this.entries.get(key);
    }

    private Map<String, String> load(final ConfigurationNode node) {
        Map<String, String> results = new HashMap<>();

        for (final ConfigurationNode value : node.childrenMap().values()) {
            if (value.isMap()) {
                for (final ConfigurationNode childrenValue : value.childrenMap().values()) {
                    results.put(value.key() + "." + childrenValue.key(), childrenValue.getString());
                }
                continue;
            }

            results.put((String) value.key(), value.getString());
        }

        return results;
    }

    //todo: NEEDS TO COPY ALL LOCALES REGARDLESS
    private CommentedConfigurationNode loadDefault(final String locale) throws ConfigurateException {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .url(PureTickets.class.getResource("/locales/" + this.makeLocaleFileName(locale)))
                .build();

        return loader.load();
    }

    private String makeLocaleFileName(final String locale) {
        return "locale_" + locale + ".yml";
    }

}
