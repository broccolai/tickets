package love.broccolai.tickets.common.packaged;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PackagedMigrations {
    private PackagedMigrations() {
    }

    public static void migrate(final ClassLoader classLoader, final DataSource dataSource) {
        Flyway.configure(classLoader)
            .baselineOnMigrate(true)
            .locations("classpath:queries/migrations")
            .dataSource(dataSource)
            .load()
            .migrate();
    }
}
