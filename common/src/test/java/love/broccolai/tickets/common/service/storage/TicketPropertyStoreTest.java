package love.broccolai.tickets.common.service.storage;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import love.broccolai.tickets.common.utilities.QueriesLocator;
import love.broccolai.tickets.common.utilities.TicketsH2Extension;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketPropertyStoreTest {

    @RegisterExtension
    JdbiExtension h2Extension = TicketsH2Extension.instance();

    private final QueriesLocator locator = new QueriesLocator(DatabaseConfiguration.Type.H2);
    private final TicketPropertyStore properties = new TicketPropertyStore(this.locator);

    @Test
    void savesAndLoadsActionProperties() {
        this.h2Extension.getJdbi().useHandle(handle -> {
            int actionId = this.createEvent(handle);

            this.properties.save(handle, actionId, Map.of(
                "message", "needs staff eyes",
                "weight", 2
            ));

            assertThat(this.properties.loadAction(handle, actionId).decoded())
                .containsExactly("message", "needs staff eyes", "weight", 2);
        });
    }

    @Test
    void roundTripsSupportedValueTypes() {
        this.h2Extension.getJdbi().useHandle(handle -> {
            int actionId = this.createEvent(handle);
            UUID uniqueId = UUID.randomUUID();
            Instant occurredAt = Instant.parse("2026-04-24T12:30:00Z");
            Profile profile = new Profile(UUID.randomUUID(), "Broccoli");
            Location location = new Location("world", 1.25, 2.5, 3.75, 90.0F, 45.0F);

            this.properties.save(handle, actionId, Map.ofEntries(
                Map.entry("string", "hello"),
                Map.entry("uuid", uniqueId),
                Map.entry("instant", occurredAt),
                Map.entry("boolean", true),
                Map.entry("int", 12),
                Map.entry("long", 13L),
                Map.entry("float", 14.5F),
                Map.entry("double", 15.5D),
                Map.entry("profile", profile),
                Map.entry("location", location),
                Map.entry("enum", Example.FANCY)
            ));

            assertThat(this.properties.loadAction(handle, actionId).decoded()).containsExactly(
                "string", "hello",
                "uuid", uniqueId,
                "instant", occurredAt,
                "boolean", true,
                "int", 12,
                "long", 13L,
                "float", 14.5F,
                "double", 15.5D,
                "profile", profile,
                "location", location,
                "enum", "FANCY"
            );
        });
    }

    @Test
    void rejectsUnsupportedValues() {
        this.h2Extension.getJdbi().useHandle(handle -> {
            int actionId = this.createEvent(handle);

            assertThrows(IllegalArgumentException.class, () -> {
                this.properties.save(handle, actionId, Map.of("bad", new Object()));
            });
        });
    }

    private int createTicket(final Handle handle) {
        return handle.createUpdate(this.locator.query("insert-ticket"))
            .bindByType("created_at", Instant.parse("2026-04-24T12:00:00Z"), Instant.class)
            .executeAndReturnGeneratedKeys()
            .mapTo(Integer.class)
            .one();
    }

    private int createEvent(final Handle handle) {
        return this.createEvent(handle, this.createTicket(handle));
    }

    private int createEvent(final Handle handle, final int ticketId) {
        return handle.createUpdate(this.locator.query("insert-action"))
            .bind("ticket", ticketId)
            .bind("action_type", "test:action")
            .bindByType("actor", UUID.randomUUID(), UUID.class)
            .bindByType("occurred_at", Instant.parse("2026-04-24T12:01:00Z"), Instant.class)
            .executeAndReturnGeneratedKeys()
            .mapTo(Integer.class)
            .one();
    }

    private enum Example {
        FANCY
    }
}
