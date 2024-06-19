package love.broccolai.tickets.api.model.format;

import java.util.HashMap;
import love.broccolai.tickets.api.utilities.Pair;

//todo: improve
public final class TicketFormatContent extends HashMap<String, String> {

    @SafeVarargs
    public static TicketFormatContent of(final Pair<String, String>... data) {
        TicketFormatContent content = new TicketFormatContent();
        for (Pair<String, String> pair : data) {
            content.put(pair.first(), pair.second());
        }

        return content;
    }
}
