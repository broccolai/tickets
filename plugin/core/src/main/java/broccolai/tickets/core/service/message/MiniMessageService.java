package broccolai.tickets.core.service.message;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.template.TemplateService;
import broccolai.tickets.core.configuration.LocaleConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Singleton
public final class MiniMessageService implements MessageService {

    private final LocaleConfiguration locale;
    private final TemplateService templateService;

    private final Template prefix;

    @Inject
    public MiniMessageService(
            final @NonNull LocaleConfiguration locale,
            final @NonNull TemplateService templateService
    ) {
        this.locale = locale;
        this.templateService = templateService;
        this.prefix = Template.of("prefix", this.locale.prefix.use());
    }

    @Override
    public Component senderTicketCreation(final @NonNull Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.create.use(templates);
    }

    @Override
    public Component senderTicketReopen(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.reopen.use(templates);
    }

    @Override
    public Component senderTicketUpdate(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.update.use(templates);
    }

    @Override
    public Component senderTicketClose(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.close.use(templates);
    }

    @Override
    public Component senderTicketClaim(final @NonNull Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.claim.use(templates);
    }

    @Override
    public Component senderTicketUnclaim(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.unclaim.use(templates);
    }

    @Override
    public Component senderTicketAssign(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.assign.use(templates);
    }

    @Override
    public Component senderTicketComplete(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.complete.use(templates);
    }

    @Override
    public Component senderTicketNote(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.note.use(templates);
    }

    @Override
    public Component targetTicketClaim(final @NonNull Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.notify.claim.use(templates);
    }

    @Override
    public Component targetTicketReopen(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.notify.reopen.use(templates);
    }

    @Override
    public Component targetTicketUnclaim(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.notify.unclaim.use(templates);
    }

    @Override
    public Component targetTicketComplete(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.notify.complete.use(templates);
    }

    @Override
    public Component targetTicketNote(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.notify.note.use(templates);
    }

    @Override
    public Component staffTicketClaim(final @NonNull Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.claim.use(templates);
    }

    @Override
    public Component staffTicketUnclaim(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.unclaim.use(templates);
    }

    @Override
    public Component staffTicketAssign(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.assign.use(templates);
    }

    @Override
    public Component staffTicketComplete(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.complete.use(templates);
    }

    @Override
    public Component staffTicketNote(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.note.use(templates);
    }

    @Override
    public Component staffTicketCreate(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.create.use(templates);
    }

    @Override
    public Component staffTicketReopen(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.reopen.use(templates);
    }

    @Override
    public Component staffTicketUpdate(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.update.use(templates);
    }

    @Override
    public Component staffTicketClose(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.close.use(templates);
    }

    @Override
    public Component commandsTicketList(final @NonNull Collection<@NonNull Ticket> tickets) {
        Template wrapper = Template.of("wrapper", this.locale.title.wrapper.use());

        TextComponent.Builder builder = Component.text()
                .append(this.locale.title.yourTickets.use(Collections.singletonList(wrapper)));

        tickets.forEach(ticket -> {
            List<Template> templates = new ArrayList<>(this.templateService.ticket(ticket));
            templates.add(this.prefix);

            Component list = this.locale.format.list.use(templates);
            builder.append(Component.newline(), list);
        });

        return builder.build();
    }

    @Override
    public Component showTicket(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return Component.join(
                Component.newline(),
                this.locale.title.showTicket.use(templates),
                this.locale.show.status.use(templates),
                this.locale.show.player.use(templates),
                this.locale.show.position.use(templates),
                this.locale.show.message.use(templates)
        );
    }

    @Override
    public Component taskReminder(final int count) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.add(Template.of("amount", String.valueOf(count)));

        return this.locale.format.reminder.use(templates);
    }

}
