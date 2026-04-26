package love.broccolai.tickets.common.serialization.jdbi;

import java.util.UUID;
import love.broccolai.tickets.api.model.profile.Profile;
import org.jdbi.v3.core.mapper.RowViewMapper;
import org.jdbi.v3.core.result.RowView;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ProfileMapper implements RowViewMapper<Profile> {

    @Override
    public Profile map(final RowView row) {
        return new Profile(
            row.getColumn("uuid", UUID.class),
            row.getColumn("username", String.class)
        );
    }
}
