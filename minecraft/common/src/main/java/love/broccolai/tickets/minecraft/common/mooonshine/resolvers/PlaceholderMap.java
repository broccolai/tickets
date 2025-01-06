package love.broccolai.tickets.minecraft.common.mooonshine.resolvers;

import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@NullMarked
final class PlaceholderMap {

    private final Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> result = new HashMap<>();

    private final String baseIdentifier;

    PlaceholderMap(String baseIdentifier) {
        this.baseIdentifier = baseIdentifier;
    }

    public <F> PlaceholderMap continuance(final String key, final F value, final Type type) {
        this.result.put(
            createKey(key),
            Either.right(ContinuanceValue.continuanceValue(value, type))
        );

        return this;
    }

    public PlaceholderMap conclusion(final String key, final Component component) {
        this.result.put(
            createKey(key),
            Either.left(ConclusionValue.conclusionValue(component))
        );
        return this;
    }

    public Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> build() {
        return this.result;
    }

    private String createKey(final String key) {
        return this.baseIdentifier + "_" + key;
    }

}
