package love.broccolai.tickets.minecraft.common.service;

import org.jspecify.annotations.NullMarked;

@NullMarked
final class TicketLabels {

    private TicketLabels() {
    }

    static String title(final String key) {
        String raw = key.substring(key.indexOf(":") + 1);
        String[] words = raw.split("[._-]");
        StringBuilder title = new StringBuilder();

        for (String word : words) {
            if (word.isBlank()) {
                continue;
            }

            if (!title.isEmpty()) {
                title.append(" ");
            }

            title.append(Character.toUpperCase(word.charAt(0)));
            title.append(word.substring(1));
        }

        if (title.isEmpty()) {
            return key;
        }

        return title.toString();
    }
}
