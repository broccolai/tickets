package broccolai.tickets.core.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.function.Function;

public enum Message {

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
    ANNOUNCEMENT__NOTE_TICKET,
    // format
    FORMAT__SHOW,
    FORMAT__LIST,
    FORMAT__LIST_HEADER,
    FORMAT__LOG,
    FORMAT__STATUS,
    FORMAT__HS,
    FORMAT__SETTING_UPDATE,
    FORMAT__REMINDER,
    // title
    TITLE__SPECIFIC_TICKETS,
    TITLE__YOUR_TICKETS,
    TITLE__ALL_TICKETS,
    TITLE__SPECIFIC_STATUS,
    TITLE__TICKET_STATUS,
    TITLE__SHOW_TICKET,
    TITLE__TICKET_LOG,
    TITLE__HIGHSCORES,
    // exceptions
    EXCEPTION__TICKET_NOT_FOUND,
    EXCEPTION__INVALID_SETTING_TYPE,
    EXCEPTION__TOO_MANY_OPEN_TICKETS,
    EXCEPTION__TICKET_CLOSED,
    EXCEPTION__TICKET_OPEN;

    private static final MiniMessage MINI = MiniMessage.get();

    private String component;

    /**
     * @param templates s
     * @return a
     */
    public @NonNull Component use(final @NonNull Template... templates) {
        Template[] allTemplates = Message.withPrefix(templates);

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
        for (Message value : Message.values()) {
            value.component = nodeToRaw.apply(value.name().toLowerCase());
        }
    }
}
