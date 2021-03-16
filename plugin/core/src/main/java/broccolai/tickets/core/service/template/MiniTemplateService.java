package broccolai.tickets.core.service.template;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.service.template.TemplateService;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Collection;

public final class MiniTemplateService implements TemplateService {

    @Override
    public Collection<Template> ticket(@NonNull final Ticket ticket) {
        return Arrays.asList(
                Template.of("ticket", String.valueOf(ticket.id())),
                Template.of("message", ticket.message().message())
        );
    }

}
