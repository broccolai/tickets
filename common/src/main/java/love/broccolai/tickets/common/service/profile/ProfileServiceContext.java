package love.broccolai.tickets.common.service.profile;

import java.util.Collection;
import java.util.UUID;
import love.broccolai.tickets.api.model.proflie.Profile;
import org.incendo.cloud.services.ChunkedRequestContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ProfileServiceContext extends ChunkedRequestContext<UUID, Profile> {

    public ProfileServiceContext(final Collection<UUID> requests) {
        super(requests);
    }

}
