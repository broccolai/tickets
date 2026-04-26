package love.broccolai.tickets.common.service.storage;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import love.broccolai.tickets.api.action.TicketActionListener;
import love.broccolai.tickets.api.model.action.AssociatedTicketAction;
import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import love.broccolai.tickets.common.utilities.QueriesLocator;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NullMarked
public final class TicketNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(TicketNotificationListener.class);

    private final Jdbi jdbi;
    private final DatabaseConfiguration.Type databaseType;
    private final QueriesLocator locator;
    private final IntFunction<AssociatedTicketAction> actionLookup;
    private final List<Handle> listenerHandles = new ArrayList<>();

    public TicketNotificationListener(
        final Jdbi jdbi,
        final DatabaseConfiguration.Type databaseType,
        final IntFunction<AssociatedTicketAction> actionLookup
    ) {
        this.jdbi = jdbi;
        this.databaseType = databaseType;
        this.locator = new QueriesLocator(databaseType);
        this.actionLookup = actionLookup;
    }

    public void add(final TicketActionListener listener) {
        if (this.databaseType != DatabaseConfiguration.Type.POSTGRES) {
            logger.debug("Skipping action listener registration for database type {}", this.databaseType);
            return;
        }

        Handle handle = this.jdbi.open();
        handle.execute(this.locator.query("listen-ticket-actions"));

        PGConnection connection = this.connectionFromHandle(handle);
        logger.trace("Adding action listener: {}", listener.getClass().getSimpleName());
        connection.addNotificationListener(new PGNotificationListener() {
            @Override
            public void notification(final int processId, final String channelName, final String payload) {
                listener.actionCreated(TicketNotificationListener.this.actionLookup.apply(Integer.parseInt(payload)));
            }
        });

        this.listenerHandles.add(handle);
    }

    private PGConnection connectionFromHandle(final Handle handle) {
        try {
            Connection connection = handle.getConnection();
            return connection.unwrap(PGConnection.class);
        } catch (SQLException exception) {
            throw new RuntimeException("Error obtaining PGConnection", exception);
        }
    }
}
