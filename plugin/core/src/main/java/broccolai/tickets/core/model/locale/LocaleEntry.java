package broccolai.tickets.core.model.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public final class LocaleEntry {

    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private final String serialised;

    public LocaleEntry(final @NonNull String serialised) {
        this.serialised = serialised;
    }

    public @NonNull Component use() {
        return MINI.deserialize(this.serialised);
    }

    public @NonNull Component use(final @NonNull List<TagResolver> templates) {
        return MINI.deserialize(this.serialised, templates.toArray(TagResolver[]::new));
    }

    @Override
    public @NonNull String toString() {
        return this.serialised;
    }

}
