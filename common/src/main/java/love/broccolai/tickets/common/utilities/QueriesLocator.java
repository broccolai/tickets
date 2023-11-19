package love.broccolai.tickets.common.utilities;

import com.google.common.base.Splitter;
import java.util.List;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.core.locator.internal.ClasspathBuilder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QueriesLocator {

    private static final String SQL_EXTENSION = "sql";
    private static final Splitter SPLITTER = Splitter.on(';');
    private final ClasspathSqlLocator locator = ClasspathSqlLocator.create();

    public String query(final String name) {
        return this.locator.getResource(
            QueriesLocator.class.getClassLoader(),
            this.createQueryPath(name)
        );
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

}
