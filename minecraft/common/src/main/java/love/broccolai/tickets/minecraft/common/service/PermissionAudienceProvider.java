package love.broccolai.tickets.minecraft.common.service;

import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface PermissionAudienceProvider {

    Audience audience(String permission, @Nullable UUID excluded);
}
