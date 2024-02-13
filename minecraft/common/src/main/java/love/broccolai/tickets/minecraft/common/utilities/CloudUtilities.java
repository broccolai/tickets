package love.broccolai.tickets.minecraft.common.utilities;

import org.incendo.cloud.parser.ArgumentParseResult;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CloudUtilities {

    private CloudUtilities() {
    }

    public static <T> T unwrapParseResult(final ArgumentParseResult<T> result) throws Throwable {
        return result.parsedValue()
            .orElseThrow(() -> result.failure().get());
    }
}
