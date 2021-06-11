package broccolai.tickets.core.model.locale;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

public record LocaleEntry(String serialised) {

    private static final MiniMessage MINI = MiniMessage.get();

    public LocaleEntry(final @NonNull String serialised) {
        this.serialised = serialised;
    }

    public @NonNull Component use() {
        return MINI.parse(this.serialised);
    }

    public @NonNull Component use(final @NonNull List<Template> templates) {
        return MINI.parse(this.serialised, templates);
    }

}
