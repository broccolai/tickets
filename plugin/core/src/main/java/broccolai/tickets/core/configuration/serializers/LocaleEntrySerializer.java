package broccolai.tickets.core.configuration.serializers;

import broccolai.tickets.core.model.locale.LocaleEntry;
import java.lang.reflect.Type;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public final class LocaleEntrySerializer implements TypeSerializer<LocaleEntry> {

    public static final LocaleEntrySerializer INSTANCE = new LocaleEntrySerializer();

    @Override
    public LocaleEntry deserialize(final Type type, final ConfigurationNode node) {
        return node.getString() != null ? new LocaleEntry(node.getString()) : null;
    }

    @Override
    public void serialize(
            final Type type,
            final @Nullable LocaleEntry localeEntry,
            final ConfigurationNode node
    ) throws SerializationException {
        if (localeEntry == null) {
            node.raw(null);
            return;
        }

        node.set(localeEntry.serialised());
    }

}
