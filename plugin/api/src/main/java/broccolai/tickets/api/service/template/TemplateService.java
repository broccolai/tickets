package broccolai.tickets.api.service.template;

import broccolai.tickets.api.model.ticket.Ticket;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public interface TemplateService {

    Collection<Template> ticket(@NonNull Ticket ticket);

}
