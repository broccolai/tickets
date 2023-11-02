package broccolai.tickets.api.service.template;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public interface TemplateService {

    @NonNull List<@NonNull TagResolver> player(@NonNull String prefix, @NonNull Soul soul);

    @NonNull List<@NonNull TagResolver> ticket(@NonNull Ticket ticket);

}
