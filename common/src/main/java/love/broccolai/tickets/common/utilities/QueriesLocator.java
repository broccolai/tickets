package love.broccolai.tickets.common.utilities;

import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QueriesLocator {

    private static final String SQL_EXTENSION = "sql";

    private final ClasspathSqlLocator locator = ClasspathSqlLocator.create();

    private final DatabaseConfiguration.Type databaseType;

    public QueriesLocator(final DatabaseConfiguration.Type databaseType) {
        this.databaseType = databaseType;
    }

    public String query(final String name) {
        String rawQuery = this.locator.getResource(
            QueriesLocator.class.getClassLoader(),
            this.createQueryPath(name)
        );

        return this.applyDialectSpecificAlterations(rawQuery);
    }

    private String createQueryPath(final String name) {
        return "queries/" + name + "." + SQL_EXTENSION;
    }

    private String applyDialectSpecificAlterations(final String query) {
        String alteredQuery = query;

        if (this.databaseType == DatabaseConfiguration.Type.POSTGRES) {
            // FORMAT JSON is only required for JSON fields in H2.
            alteredQuery = alteredQuery.replace(" FORMAT JSON", "");
        }

        return alteredQuery;
    }
}
