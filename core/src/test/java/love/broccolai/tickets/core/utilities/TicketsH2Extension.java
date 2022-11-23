package love.broccolai.tickets.core.utilities;

import org.h2.jdbcx.JdbcDataSource;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.jdbi.v3.testing.junit5.JdbiExtensionInitializer;
import org.jdbi.v3.testing.junit5.JdbiFlywayMigration;
import org.jdbi.v3.testing.junit5.JdbiH2Extension;
import javax.sql.DataSource;
import java.util.UUID;

public final class TicketsH2Extension {

    private static final JdbiExtensionInitializer FLYWAY_INITIALIZER = JdbiFlywayMigration.flywayMigration()
            .withPath("queries/migrations");

    public static JdbiExtension instance() {
        return new JdbiH2Extension() {
            @Override
            protected DataSource createDataSource() {
                JdbcDataSource ds = new JdbcDataSource();
                ds.setURL("jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE");
                ds.setUser("user");

                return ds;
            }
        }.withPlugin(new TicketsJdbiPlugin()).withInitializer(FLYWAY_INITIALIZER);
    }

    private TicketsH2Extension() {
    }
}
