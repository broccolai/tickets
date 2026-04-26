package love.broccolai.tickets.minecraft.common.service;

import java.util.Collection;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ProfileSuggestionService {

    Collection<String> usernames();
}
