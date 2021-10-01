package broccolai.tickets.api.service.template;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import java.util.List;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface TemplateService {

    @NonNull List<@NonNull Template> player(@NonNull String prefix, @NonNull Soul soul);

    @NonNull List<@NonNull Template> ticket(@NonNull Ticket ticket);

}
