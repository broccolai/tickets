package love.broccolai.tickets.common.utilities;

import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.jdbi.v3.testing.junit5.JdbiExtensionInitializer;
import org.jdbi.v3.testing.junit5.JdbiFlywayMigration;
import org.jdbi.v3.testing.junit5.JdbiH2Extension;

public final class TicketsH2Extension {

    private static final JdbiExtensionInitializer FLYWAY_INITIALIZER = JdbiFlywayMigration.flywayMigration()
        .withPath("queries/migrations");

    public static JdbiExtension instance() {
        return new JdbiH2Extension(";MODE=MySQL;DATABASE_TO_LOWER=TRUE")
            .withUser("user")
            .withPlugin(new TicketsJdbiPlugin())
            .withInitializer(FLYWAY_INITIALIZER);
    }

    private TicketsH2Extension() {
    }
}
