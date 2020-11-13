package broccolai.tickets.core.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.function.Function;

public enum NewMessages {

    PREFIX,
    // sender
    SENDER__NEW_TICKET,
    SENDER__UPDATE_TICKET,
    SENDER__CLOSE_TICKET,
    SENDER__PICK_TICKET,
    SENDER__YIELD_TICKET,
    SENDER__ASSIGN_TICKET,
    SENDER__DONE_TICKET,
    SENDER__REOPEN_TICKET,
    SENDER__NOTE_TICKET,
    SENDER__TELEPORT_TICKET,
    // notification
    NOTIFY__PICK_TICKET,
    NOTIFY__YIELD_TICKET,
    NOTIFY__ASSIGN_TICKET,
    NOTIFY__DONE_TICKET,
    NOTIFY__REOPEN_TICKET,
    NOTIFY__NOTE_TICKET,
    // announcement
    ANNOUNCEMENT__NEW_TICKET,
    ANNOUNCEMENT__UPDATE_TICKET,
    ANNOUNCEMENT__CLOSE_TICKET,
    ANNOUNCEMENT__PICK_TICKET,
    ANNOUNCEMENT__YIELD_TICKET,
    ANNOUNCEMENT__ASSIGN_TICKET,
    ANNOUNCEMENT__DONE_TICKET,
    ANNOUNCEMENT__REOPEN_TICKET,
    ANNOUNCEMENT__NOTE_TICKET;

    private static final MiniMessage MINI = MiniMessage.get();

    private String component;

    /**
     * @param templates s
     * @return a
     */
    public @NonNull Component use(final @NonNull Template... templates) {
        Template[] allTemplates = NewMessages.withPrefix(templates);

        return MINI.parse(component, allTemplates);
    }

    private static Template prefix() {
        return Template.of("prefix", PREFIX.component);
    }

    private static Template[] withPrefix(final @NonNull Template[] templates) {
        Template[] allTemplates = Arrays.copyOf(templates, templates.length + 1);

        allTemplates[templates.length] = prefix();

        return allTemplates;
    }


    /**
     * @param nodeToRaw a
     */
    public static void setup(final @NonNull Function<String, String> nodeToRaw) {
        for (NewMessages value : NewMessages.values()) {
            value.component = nodeToRaw.apply(value.name().toLowerCase());
        }
    }
}
