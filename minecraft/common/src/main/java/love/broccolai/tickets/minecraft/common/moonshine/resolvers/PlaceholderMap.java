package love.broccolai.tickets.minecraft.common.moonshine.resolvers;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.util.Either;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PlaceholderMap {

    private final Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> result = new HashMap<>();

    private final String baseIdentifier;

    PlaceholderMap() {
        this("");
    }

    PlaceholderMap(final String baseIdentifier) {
        this.baseIdentifier = baseIdentifier;
    }

    public <F> PlaceholderMap continuance(final String key, final F value, final Type type) {
        this.result.put(
            this.createKey(key),
            Either.right(ContinuanceValue.continuanceValue(value, type))
        );

        return this;
    }

    public PlaceholderMap conclusion(final String key, final Component component) {
        this.result.put(
            this.createKey(key),
            Either.left(ConclusionValue.conclusionValue(component))
        );

        return this;
    }

    public Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> build() {
        return Map.copyOf(this.result);
    }

    private String createKey(final String key) {
        if (this.baseIdentifier.isEmpty()) {
            return key;
        }

        return this.baseIdentifier + "_" + key;
    }
}
