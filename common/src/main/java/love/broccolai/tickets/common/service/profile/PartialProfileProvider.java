package love.broccolai.tickets.common.service.profile;

import io.leangen.geantyref.TypeToken;
import java.util.UUID;
import love.broccolai.tickets.api.model.proflie.Profile;
import org.incendo.cloud.services.type.PartialResultService;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PartialProfileProvider extends PartialResultService<UUID, Profile, ProfileServiceContext> {

    TypeToken<PartialProfileProvider> TYPE = TypeToken.get(PartialProfileProvider.class);

}
