package love.broccolai.tickets.common.utilities;

import com.google.common.base.Splitter;
import java.util.List;
import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.core.locator.internal.ClasspathBuilder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QueriesLocator {

    private static final String SQL_EXTENSION = "sql";
    private static final Splitter SPLITTER = Splitter.on(';');
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

    public List<String> queries(final String name) {
        return SPLITTER.splitToList(this.query(name));
    }

    private String createQueryPath(final String name) {
        return new ClasspathBuilder()
            .appendDotPath("queries")
            .appendDotPath(name)
            .setExtension(SQL_EXTENSION)
            .build();
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
