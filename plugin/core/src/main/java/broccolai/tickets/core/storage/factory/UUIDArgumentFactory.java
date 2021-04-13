package broccolai.tickets.core.storage.factory;

import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;
import java.util.UUID;

public final class UUIDArgumentFactory extends AbstractArgumentFactory<UUID> {

    public UUIDArgumentFactory() {
        super(Types.VARCHAR);
    }

    @Override
    public Argument build(final UUID value, final ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, value.toString());
    }

}
