package love.broccolai.tickets.common.packaged;

import javax.sql.DataSource;
import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import org.flywaydb.core.Flyway;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PackagedMigrations {
    private PackagedMigrations() {
    }

    public static void migrate(
        final DatabaseConfiguration configuration,
        final ClassLoader classLoader,
        final DataSource dataSource
    ) {
        String[] locations;

        if (configuration.type == DatabaseConfiguration.Type.POSTGRES) {
            locations = new String[]{"classpath:queries/migrations", "classpath:queries/psql"};
        } else {
            locations = new String[]{"classpath:queries/migrations"};
        }

        Flyway.configure(classLoader)
            .baselineOnMigrate(true)
            .locations(locations)
            .dataSource(dataSource)
            .load()
            .migrate();
    }
}
