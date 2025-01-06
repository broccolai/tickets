package love.broccolai.tickets.api.model.format;

import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.api.model.proflie.Profile;

public enum TicketFormatStyle {
    Player(Profile.class),
    Sentence(String.class),
    Location(Location.class);

    private final Class<?> contentType;

    TicketFormatStyle(final Class<?> contentType) {
        this.contentType = contentType;
    }

    public Class<?> contentType() {
        return this.contentType;
    }
}
