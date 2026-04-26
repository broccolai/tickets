package love.broccolai.tickets.common.registry;

import com.google.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.ActionCodec;
import love.broccolai.tickets.api.model.action.ActionProperties;
import love.broccolai.tickets.api.model.action.ActionRegistry;
import love.broccolai.tickets.api.model.action.packaged.TicketAssigned;
import love.broccolai.tickets.api.model.action.packaged.TicketClosed;
import love.broccolai.tickets.api.model.action.packaged.TicketCommented;
import love.broccolai.tickets.api.model.action.packaged.TicketComponentAttached;
import love.broccolai.tickets.api.model.action.packaged.TicketComponentDetached;
import love.broccolai.tickets.api.model.action.packaged.TicketDiscussionStarted;
import love.broccolai.tickets.api.model.action.packaged.TicketOpened;
import love.broccolai.tickets.api.model.action.packaged.TicketReopened;
import love.broccolai.tickets.api.model.action.packaged.TicketUnassigned;
import love.broccolai.tickets.api.model.component.ComponentProperties;
import love.broccolai.tickets.api.model.component.ComponentRegistry;
import love.broccolai.tickets.api.model.component.TicketComponent;
import love.broccolai.tickets.api.model.format.TicketFormData;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SimpleActionRegistry implements ActionRegistry {

    private final Map<String, ActionCodec<?>> codecs = new HashMap<>();
    private final ComponentRegistry componentRegistry;

    @Inject
    public SimpleActionRegistry(final ComponentRegistry componentRegistry) {
        this.componentRegistry = componentRegistry;
        this.register(ActionCodec.of(
            TicketOpened.TYPE,
            TicketOpened.class,
            this::encodeOpened,
            (date, creator, properties) -> new TicketOpened(date, creator, this.form(properties))
        ));

        this.register(ActionCodec.of(
            TicketAssigned.TYPE,
            TicketAssigned.class,
            action -> Map.of("assignee", action.assignee()),
            (date, creator, properties) -> new TicketAssigned(date, creator, properties.require("assignee", UUID.class))
        ));

        this.register(ActionCodec.of(
            TicketClosed.TYPE,
            TicketClosed.class,
            action -> Map.of(),
            (date, creator, properties) -> new TicketClosed(date, creator)
        ));

        this.register(ActionCodec.of(
            TicketReopened.TYPE,
            TicketReopened.class,
            action -> Map.of(),
            (date, creator, properties) -> new TicketReopened(date, creator)
        ));

        this.register(ActionCodec.of(
            TicketUnassigned.TYPE,
            TicketUnassigned.class,
            action -> Map.of(),
            (date, creator, properties) -> new TicketUnassigned(date, creator)
        ));

        this.register(ActionCodec.of(
            TicketCommented.TYPE,
            TicketCommented.class,
            action -> Map.of("message", action.message()),
            (date, creator, properties) -> new TicketCommented(date, creator, properties.require("message", String.class))
        ));

        this.register(ActionCodec.of(
            TicketDiscussionStarted.TYPE,
            TicketDiscussionStarted.class,
            action -> Map.of("message", action.message()),
            (date, creator, properties) -> new TicketDiscussionStarted(date, creator, properties.require("message", String.class))
        ));

        this.register(ActionCodec.of(
            TicketComponentAttached.TYPE,
            TicketComponentAttached.class,
            this::encodeAttached,
            (date, creator, properties) -> new TicketComponentAttached(date, creator, this.component(properties))
        ));

        this.register(ActionCodec.of(
            TicketComponentDetached.TYPE,
            TicketComponentDetached.class,
            action -> Map.of("component", action.key().value()),
            (date, creator, properties) -> new TicketComponentDetached(
                date,
                creator,
                this.componentRegistry.require(properties.require("component", String.class)).key()
            )
        ));
    }

    @Override
    public Collection<ActionCodec<?>> codecs() {
        return List.copyOf(this.codecs.values());
    }

    @Override
    public Optional<ActionCodec<?>> find(final String type) {
        return Optional.ofNullable(this.codecs.get(type));
    }

    @Override
    public void register(final ActionCodec<?> codec) {
        this.codecs.put(codec.type(), codec);
    }

    private Map<String, Object> encodeOpened(final TicketOpened action) {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("format", action.form().formatIdentifier());
        action.form().parts().forEach((path, value) -> values.put("part." + path, value));

        return values;
    }

    private TicketFormData form(final ActionProperties properties) {
        Map<String, Object> parts = new HashMap<>();
        properties.values().forEach((path, value) -> {
            if (path.startsWith("part.")) {
                parts.put(path.substring("part.".length()), value);
            }
        });

        return new TicketFormData(properties.require("format", String.class), parts);
    }

    private Map<String, Object> encodeAttached(final TicketComponentAttached action) {
        TicketComponent component = action.component();
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("component", component.key().value());
        this.componentRegistry.encode(component).forEach((path, value) -> values.put("component." + path, value));

        return values;
    }

    private TicketComponent component(final ActionProperties properties) {
        String componentKey = properties.require("component", String.class);
        Map<String, Object> values = new HashMap<>();
        properties.values().forEach((path, value) -> {
            if (path.startsWith("component.")) {
                values.put(path.substring("component.".length()), value);
            }
        });

        return this.componentRegistry.requireDecoded(componentKey, new ComponentProperties(values));
    }
}
