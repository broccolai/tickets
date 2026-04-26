package love.broccolai.tickets.minecraft.common.service;

import java.util.Collection;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface OnlineProfileProvider {

    Collection<String> usernames();
}
