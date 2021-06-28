package broccolai.tickets.core.service.message.moonshine;

import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.moonshine.message.IMessageRenderer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public final class MessageRenderer implements IMessageRenderer<Audience, String, Component, Component> {

    private final MiniMessage miniMessage = MiniMessage.get();

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

        return this.miniMessage.parse(intermediateMessage, templates);
    }

}
