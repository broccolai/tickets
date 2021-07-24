package broccolai.tickets.core.configuration;

import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.inject.ForTickets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationVisitor;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@Singleton
public final class NewLocaleConfiguration {

    private final Map<String, String> entries;

    @Inject
    public NewLocaleConfiguration(
            final @ForTickets Path folder,
            final MainConfiguration configuration
    ) throws ConfigurateException {
        Path localeFolder = folder.resolve("locales");
        Path file = localeFolder.resolve(this.makeLocaleFileName(configuration.locale));

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .path(file)
                .build();

        CommentedConfigurationNode node = loader.load();
        node = node.mergeFrom(this.loadDefault(configuration.locale));

        loader.save(node);
        this.entries = node.visit(new LocaleVisitor());
    }

    public String get(final String key) {
        return this.entries.get(key);
    }

    //todo: NEEDS TO COPY ALL LOCALES REGARDLESS
    private CommentedConfigurationNode loadDefault(final String locale) throws ConfigurateException {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .url(PureTickets.class.getResource("/locales/" + this.makeLocaleFileName(locale)))
                .build();

        return loader.load();
    }

    private String makeLocaleFileName(final String locale) {
        return "locale_" + locale + ".yml";
    }

    private static final class LocaleVisitor implements ConfigurationVisitor.Safe<Map<String, String>, Map<String, String>> {

        @Override
        public Map<String, String> newState() {
            return new HashMap<>();
        }

        @Override
        public void beginVisit(final ConfigurationNode node, final Map<String, String> state) {
            // no-op
        }

        @Override
        public void enterNode(final ConfigurationNode node, final Map<String, String> state) {
            // no-op
        }

        @Override
        public void enterMappingNode(final ConfigurationNode node, final Map<String, String> state) {
            // no-op
        }

        @Override
        public void enterListNode(final ConfigurationNode node, final Map<String, String> state) {
            // no-op
        }

        @Override
        public void enterScalarNode(final ConfigurationNode node, final Map<String, String> state) {
            state.put(this.path(node), node.getString());
        }

        @Override
        public void exitMappingNode(final ConfigurationNode node, final Map<String, String> state) {
            // no-op
        }

        @Override
        public void exitListNode(final ConfigurationNode node, final Map<String, String> state) {
            // no-op
        }

        @Override
        public Map<String, String> endVisit(final Map<String, String> state) {
            return state;
        }

        private String path(final ConfigurationNode node) {
            NodePath path = node.path();
            if (path.size() == 0) {
                return "";
            }

            StringBuilder sb = new StringBuilder(path.get(0).toString());
            for (int i = 1; i < path.size(); i++) {
                sb.append(".").append(path.get(i));
            }
            return sb.toString();
        }
    }

}
