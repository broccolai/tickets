package broccolai.tickets.core.service.message.moonshine;

import broccolai.tickets.core.configuration.NewLocaleConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.moonshine.message.IMessageRenderer;

@Singleton
public final class MessageRenderer implements IMessageRenderer<Audience, String, Component, Component> {

    private final MiniMessage miniMessage = MiniMessage.get();
    private final Template prefix;

    @Inject
    public MessageRenderer(final NewLocaleConfiguration localeConfiguration) {
        this.prefix = Template.of("prefix", localeConfiguration.get("prefix"));
    }

    @Override
    public Component render(
            final Audience receiver,
            final String intermediateMessage,
            final Map<String, ? extends Component> resolvedPlaceholders,
            final Method method,
            final Type owner
    ) {
        List<Template> templates = new ArrayList<>();
        resolvedPlaceholders.forEach((key, component) -> {
            templates.add(Template.of(key, component));
        });
        templates.add(this.prefix);

        return this.miniMessage.parse(intermediateMessage, templates);
    }

}
