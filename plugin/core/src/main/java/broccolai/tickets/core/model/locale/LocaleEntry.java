package broccolai.tickets.core.model.locale;

import broccolai.tickets.api.model.message.Templatable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.examination.Examinable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class LocaleEntry {

    private static final MiniMessage MINI = MiniMessage.get();

    private final String serialised;

    public LocaleEntry(final @NonNull String serialised) {
        this.serialised = serialised;
    }

    public @NonNull Component use(final @NonNull Examinable... examinables) {
        List<Template> templates = new ArrayList<>();

        for (final Examinable examinable : examinables) {
            if (examinable instanceof Template) {
                templates.add((Template) examinable);
                continue;
            }

            if (examinable instanceof Templatable) {
                Templatable templatable = (Templatable) examinable;
                templates.addAll(templatable.templates());
                continue;
            }

            throw new IllegalArgumentException();
        }

        return MINI.parse(this.serialised, templates);
    }

    @Override
    public @NonNull String toString() {
        return this.serialised;
    }

}
