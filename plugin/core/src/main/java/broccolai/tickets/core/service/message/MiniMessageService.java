package broccolai.tickets.core.service.message;

import broccolai.corn.core.Lists;
import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.template.TemplateService;
import broccolai.tickets.core.configuration.LocaleConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
    public Component senderTicketAssign(@NonNull final Ticket ticket, final @NonNull Soul target) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("target", target.uuid()));

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
    public Component targetTicketNote(@NonNull final Ticket ticket, final @NonNull String note) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.add(Template.of("note", note));
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
    public Component staffTicketNote(@NonNull final Ticket ticket, final @NonNull String note) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.add(Template.of("note", note));
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

        List<Ticket> sortedTickets = new ArrayList<>(tickets);
        sortedTickets.sort(Comparator.comparingInt(Ticket::id));

        sortedTickets.forEach(ticket -> {
            List<Template> templates = new ArrayList<>(this.templateService.ticket(ticket));
            templates.add(this.prefix);

            Component list = this.locale.format.list.use(templates);
            builder.append(Component.newline(), list);
        });

        return this.padComponent(builder.build());
    }

    @Override
    public Component commandsTicketsList(@NonNull final Map<@NonNull UUID, @NonNull Collection<@NonNull Ticket>> map) {
        Template wrapper = Template.of("wrapper", this.locale.title.wrapper.use());

        TextComponent.Builder builder = Component.text()
                .append(this.locale.title.allTickets.use(Collections.singletonList(wrapper)));

        map.forEach((uuid, tickets) -> {
            builder.append(Component.newline());
            builder.append(this.locale.format.listHeader.use(this.templateService.player("player", uuid)));

            List<Ticket> sortedTickets = new ArrayList<>(tickets);
            sortedTickets.sort(Comparator.comparingInt(Ticket::id));

            for (final Ticket ticket : sortedTickets) {
                List<Template> templates = new ArrayList<>(this.templateService.ticket(ticket));
                templates.add(this.prefix);

                Component list = this.locale.format.list.use(templates);
                builder.append(Component.newline(), list);
            }
        });

        return this.padComponent(builder.build());
    }

    @Override
    public Component commandsTeleport(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.teleport.use(templates);
    }

    @Override
    public Component commandsHighscore(@NonNull final Map<UUID, Integer> ranks) {
        Template wrapper = Template.of("wrapper", this.locale.title.wrapper.use());

        TextComponent.Builder builder = Component.text()
                .append(this.locale.title.highscores.use(Collections.singletonList(wrapper)), Component.newline());

        List<Component> entries = Lists.map(ranks.entrySet(), (entry) -> {
            List<Template> templates = new ArrayList<>(this.templateService.player("player", entry.getKey()));
            templates.add(Template.of("amount", entry.getValue().toString()));

            return this.locale.format.hs.use(templates);
        });

        return builder.append(Component.join(
                Component.newline(),
                entries
        )).build();
    }

    @Override
    public Component commandsLog(@NonNull final Collection<Interaction> interactions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        Template wrapper = Template.of("wrapper", this.locale.title.wrapper.use());

        TextComponent.Builder builder = Component.text()
                .append(this.locale.title.highscores.use(Collections.singletonList(wrapper)), Component.newline());

        List<Component> entries = Lists.map(interactions, (interaction) -> {
            List<Template> templates = new ArrayList<>(this.templateService.player("player", interaction.sender()));
            templates.add(Template.of("action", interaction.action().name()));

            Component hoverComponent = Component.text("Time: " + formatter.format(interaction.time()));
            hoverComponent = hoverComponent.append(Component.newline());

            if (interaction instanceof MessageInteraction) {
                MessageInteraction messageInteraction = (MessageInteraction) interaction;
                hoverComponent = hoverComponent.append(Component.text("Message: " + messageInteraction.message()));
            }

            return this.locale.format.log.use(templates).hoverEvent(HoverEvent.showText(hoverComponent));
        });

        return builder.append(Component.join(
                Component.newline(),
                entries
        )).build();
    }

    @Override
    public Component exceptionTicketOpen() {
        return this.locale.exception.ticketOpen.use(Collections.singletonList(this.prefix));
    }

    @Override
    public Component exceptionTicketClaimed() {
        return this.locale.exception.ticketClaimed.use(Collections.singletonList(this.prefix));
    }

    @Override
    public Component exceptionTicketClosed() {
        return this.locale.exception.ticketClosed.use(Collections.singletonList(this.prefix));
    }

    @Override
    public Component exceptionTicketNotFound() {
        return this.locale.exception.ticketNotFound.use(Collections.singletonList(this.prefix));
    }

    @Override
    public Component exceptionTooManyTicketsOpen() {
        return this.locale.exception.tooManyTicketsOpen.use(Collections.singletonList(this.prefix));
    }

    @Override
    public Component exceptionNoPermission() {
        return this.locale.exception.noPermission.use(Collections.singletonList(this.prefix));
    }

    @Override
    public Component exceptionWrongSender(@NonNull final Class<?> sender) {
        Template template = Template.of("sender", sender.getSimpleName());
        return this.locale.exception.invalidSender.use(Arrays.asList(this.prefix, template));
    }

    @Override
    public Component showTicket(@NonNull final Ticket ticket) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.add(Template.of("wrapper", this.locale.title.wrapper.use()));
        templates.addAll(this.templateService.ticket(ticket));

        return this.padComponent(Component.join(
                Component.newline(),
                this.locale.title.showTicket.use(templates),
                this.locale.show.status.use(templates),
                this.locale.show.player.use(templates),
                this.locale.show.position.use(templates),
                this.locale.show.message.use(templates)
        ));
    }

    @Override
    public Component taskReminder(final int count) {
        List<Template> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.add(Template.of("amount", String.valueOf(count)));

        return this.locale.format.reminder.use(templates);
    }

    private Component padComponent(final @NonNull Component component) {
        return TextComponent.ofChildren(
                Component.newline(),
                component,
                Component.newline()
        );
    }

}
