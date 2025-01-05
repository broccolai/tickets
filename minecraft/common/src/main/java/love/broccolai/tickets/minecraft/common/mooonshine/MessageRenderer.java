package love.broccolai.tickets.minecraft.common.mooonshine;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.moonshine.message.IMessageRenderer;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class MessageRenderer implements IMessageRenderer<Audience, String, Component, Component> {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Inject
    public MessageRenderer() {
    }

    @Override
    public Component render(
        final Audience receiver,
        final String intermediateMessage,
        final Map<String, ? extends Component> resolvedPlaceholders,
        final Method method,
        final Type owner
    ) {
        TagResolver resolver = TagResolver.builder()
            .caching(name -> {
                Component placeholder = resolvedPlaceholders.get(name);

                if (placeholder == null) {
                    return null;
                }

                return Tag.selfClosingInserting(placeholder);
            })
            .build();

        return this.miniMessage.deserialize(intermediateMessage, resolver);
    }

}
