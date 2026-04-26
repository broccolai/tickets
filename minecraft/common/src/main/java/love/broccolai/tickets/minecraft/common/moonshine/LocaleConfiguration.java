package love.broccolai.tickets.minecraft.common.moonshine;

import java.util.HashMap;
import java.util.Map;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationVisitor;
import org.spongepowered.configurate.NodePath;

public final class LocaleConfiguration extends HashMap<String, String> {

    private static final long serialVersionUID = 1L;

    public static final class Visitor implements ConfigurationVisitor.Safe<Map<String, String>, LocaleConfiguration> {

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
        public LocaleConfiguration endVisit(final Map<String, String> state) {
            LocaleConfiguration configuration = new LocaleConfiguration();
            configuration.putAll(state);

            return configuration;
        }

        private String path(final ConfigurationNode node) {
            NodePath path = node.path();

            if (path.size() == 0) {
                return "";
            }

            StringBuilder pathBuilder = new StringBuilder(path.get(0).toString());

            for (int index = 1; index < path.size(); index++) {
                pathBuilder.append(".").append(path.get(index));
            }

            return pathBuilder.toString();
        }
    }
}
