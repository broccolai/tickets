package broccolai.tickets.core.service.template;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.service.template.TemplateService;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public final class MiniTemplateService implements TemplateService {

    private final UserService userService;

    @Inject
    public MiniTemplateService(final @NonNull UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<Template> user(@NonNull final UUID uuid) {
        //todo template for hoverable component too
        return Arrays.asList(
                Template.of("name", this.userService.name(uuid)),
                Template.of("uuid", uuid.toString())
        );
    }

    @Override
    public List<Template> ticket(@NonNull final Ticket ticket) {
        return Arrays.asList(
                Template.of("ticket", String.valueOf(ticket.id())),
                Template.of("message", ticket.message().message())
        );
    }

}
