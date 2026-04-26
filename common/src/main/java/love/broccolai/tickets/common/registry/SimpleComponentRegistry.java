package love.broccolai.tickets.common.registry;

import com.google.inject.Inject;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.component.ComponentCodec;
import love.broccolai.tickets.api.model.component.ComponentProperties;
import love.broccolai.tickets.api.model.component.ComponentRegistry;
import love.broccolai.tickets.api.model.component.TicketAssignment;
import love.broccolai.tickets.api.model.component.TicketCreatedAt;
import love.broccolai.tickets.api.model.component.TicketCreator;
import love.broccolai.tickets.api.model.component.TicketForm;
import love.broccolai.tickets.api.model.component.TicketStatusComponent;
import love.broccolai.tickets.api.model.component.TicketType;
import love.broccolai.tickets.api.model.format.TicketFormData;
import love.broccolai.tickets.api.registry.TicketTypeRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SimpleComponentRegistry implements ComponentRegistry {

    private final Map<String, ComponentCodec<?>> codecs = new HashMap<>();

    @Inject
    public SimpleComponentRegistry(final TicketTypeRegistry ticketTypeRegistry) {
        this.register(ComponentCodec.of(
            TicketType.KEY,
            component -> Map.of("identifier", component.format().identifier()),
            properties -> new TicketType(ticketTypeRegistry.requireIdentifier(properties.require("identifier", String.class)))
        ));

        this.register(ComponentCodec.of(
            TicketCreator.KEY,
            component -> Map.of("creator", component.creator()),
            properties -> new TicketCreator(properties.require("creator", UUID.class))
        ));

        this.register(ComponentCodec.of(
            TicketCreatedAt.KEY,
            component -> Map.of("date", component.date()),
            properties -> new TicketCreatedAt(properties.require("date", Instant.class))
        ));

        this.register(ComponentCodec.of(
            TicketStatusComponent.KEY,
            component -> Map.of("status", component.status().name()),
            properties -> new TicketStatusComponent(TicketStatus.valueOf(properties.require("status", String.class)))
        ));

        this.register(ComponentCodec.of(
            TicketAssignment.KEY,
            component -> Map.of("assignee", component.assignee()),
            properties -> new TicketAssignment(properties.require("assignee", UUID.class))
        ));

        this.register(ComponentCodec.of(
            TicketForm.KEY,
            this::encodeForm,
            this::decodeForm
        ));
    }

    @Override
    public Collection<ComponentCodec<?>> codecs() {
        return List.copyOf(this.codecs.values());
    }

    @Override
    public Optional<ComponentCodec<?>> find(final String key) {
        return Optional.ofNullable(this.codecs.get(key));
    }

    @Override
    public void register(final ComponentCodec<?> codec) {
        this.codecs.put(codec.key().value(), codec);
    }

    private Map<String, Object> encodeForm(final TicketForm component) {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("format", component.data().formatIdentifier());
        component.data().parts().forEach((path, value) -> values.put("part." + path, value));

        return values;
    }

    private TicketForm decodeForm(final ComponentProperties properties) {
        Map<String, Object> parts = new HashMap<>();
        properties.values().forEach((path, value) -> {
            if (path.startsWith("part.")) {
                parts.put(path.substring("part.".length()), value);
            }
        });

        return new TicketForm(new TicketFormData(properties.require("format", String.class), parts));
    }
}
