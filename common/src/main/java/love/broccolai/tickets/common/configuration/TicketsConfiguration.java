package love.broccolai.tickets.common.configuration;

import com.google.inject.Singleton;
import java.util.List;
import love.broccolai.tickets.api.model.TicketType;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Singleton
@ConfigSerializable
@NullMarked
public final class TicketsConfiguration implements Configuration {

    public List<TicketType> types = List.of(
        new TicketType(
            "question",
            "Question",
            "Ask a general question!"
        ),
        new TicketType(
            "bug_report",
            "Bug Report",
            "Report a bug!"
        ),
        new TicketType(
            "feature_request",
            "Feature Request",
            "Request a feature!"
        )
    );

}
