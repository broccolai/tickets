package broccolai.tickets.core.model.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public final class LocaleEntry {

    private static final MiniMessage MINI = MiniMessage.get();

    private final String serialised;

    public LocaleEntry(final @NonNull String serialised) {
        this.serialised = serialised;
    }

    public @NonNull Component use() {
        return MINI.parse(this.serialised);
    }

    public @NonNull Component use(final @NonNull Collection<Template> templates) {
        return MINI.parse(this.serialised, templates);
    }

    @Override
    public @NonNull String toString() {
        return this.serialised;
    }

}
