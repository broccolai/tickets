package broccolai.tickets.api.service.template;

import broccolai.tickets.api.model.ticket.Ticket;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.UUID;

public interface TemplateService {

    List<Template> player(@NonNull UUID uuid);

    List<Template> ticket(@NonNull Ticket ticket);

}
