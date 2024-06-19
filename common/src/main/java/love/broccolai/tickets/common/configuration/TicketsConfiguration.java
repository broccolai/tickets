package love.broccolai.tickets.common.configuration;

import com.google.inject.Singleton;
import java.util.List;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.format.TicketFormatPart;
import love.broccolai.tickets.api.model.format.TicketFormatStyle;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Singleton
@ConfigSerializable
@NullMarked
public final class TicketsConfiguration implements Configuration {

    public List<TicketFormat> types = List.of(
        new TicketFormat(
            "question",
            "Question",
            "Ask a general question!",
            List.of(
                new TicketFormatPart("message", TicketFormatStyle.Sentence)
            )
        ),
        new TicketFormat(
            "bug_report",
            "Bug Report",
            "Report a bug!",
            List.of(
                new TicketFormatPart("message", TicketFormatStyle.Sentence)
            )
        ),
        new TicketFormat(
            "feature_request",
            "Feature Request",
            "Request a feature!",
            List.of(
                new TicketFormatPart("message", TicketFormatStyle.Sentence)
            )
        ),
        new TicketFormat(
            "report_player",
            "Report Player",
            "Report a player.",
            List.of(
                new TicketFormatPart("player", TicketFormatStyle.Player),
                new TicketFormatPart("message", TicketFormatStyle.Sentence)
            )
        )
    );

}
