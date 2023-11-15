package love.broccolai.tickets.common.storage;

import java.sql.Types;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.json.EncodedJson;

@EncodedJson
public class JsonArgumentFactory extends AbstractArgumentFactory<String> {
    public JsonArgumentFactory() {
        super(Types.OTHER);
    }

    @Override
    protected Argument build(String value, ConfigRegistry config) {
        return (p, s, c) -> s.setObject(p, value, Types.OTHER);
    }
}
