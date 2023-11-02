package broccolai.tickets.core.service.message;

import broccolai.corn.core.Lists;
import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.template.TemplateService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.core.configuration.LocaleConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public final class MiniMessageService implements MessageService {

    private final LocaleConfiguration locale;
    private final UserService userService;
    private final TemplateService templateService;

    private final TagResolver prefix;

    @Inject
    public MiniMessageService(
            final @NonNull LocaleConfiguration locale,
            final @NonNull UserService userService,
            final @NonNull TemplateService templateService
    ) {
        this.locale = locale;
        this.userService = userService;
        this.templateService = templateService;
        this.prefix = TagResolver.resolver("prefix", Tag.selfClosingInserting(this.locale.prefix.use()));
    }

    @Override
    public @NonNull Component senderTicketCreation(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.create.use(templates);
    }

    @Override
    public @NotNull Component senderTicketReopen(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.reopen.use(templates);
    }

    @Override
    public @NotNull Component senderTicketUpdate(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.update.use(templates);
    }

    @Override
    public @NotNull Component senderTicketClose(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.close.use(templates);
    }

    @Override
    public @NotNull Component senderTicketClaim(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.claim.use(templates);
    }

    @Override
    public @NotNull Component senderTicketUnclaim(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.unclaim.use(templates);
    }

    @Override
    public @NotNull Component senderTicketAssign(final @NonNull Ticket ticket, final @NonNull Soul target) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("target", target));

        return this.locale.sender.assign.use(templates);
    }

    @Override
    public @NotNull Component senderTicketComplete(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.complete.use(templates);
    }

    @Override
    public @NotNull Component senderTicketNote(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.note.use(templates);
    }

    @Override
    public @NotNull Component targetTicketClaim(final @NonNull Ticket ticket, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));

        return this.locale.notify.claim.use(templates);
    }

    @Override
    public @NotNull Component targetTicketReopen(final @NonNull Ticket ticket, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));

        return this.locale.notify.reopen.use(templates);
    }

    @Override
    public @NotNull Component targetTicketUnclaim(final @NonNull Ticket ticket, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));

        return this.locale.notify.unclaim.use(templates);
    }

    @Override
    public @NotNull Component targetTicketComplete(final @NonNull Ticket ticket, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));

        return this.locale.notify.complete.use(templates);
    }

    @Override
    public @NotNull Component targetTicketNote(final @NonNull Ticket ticket, final @NonNull String note, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.add(TagResolver.resolver("note", Tag.preProcessParsed(note)));
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));

        return this.locale.notify.note.use(templates);
    }

    @Override
    public @NotNull Component staffTicketClaim(final @NonNull Ticket ticket, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));

        return this.locale.announcement.claim.use(templates);
    }

    @Override
    public @NotNull Component staffTicketUnclaim(final @NonNull Ticket ticket, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));

        return this.locale.announcement.unclaim.use(templates);
    }

    @Override
    public @NotNull Component staffTicketAssign(final @NonNull Ticket ticket, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));

        return this.locale.announcement.assign.use(templates);
    }

    @Override
    public @NotNull Component staffTicketComplete(final @NonNull Ticket ticket, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));

        return this.locale.announcement.complete.use(templates);
    }

    @Override
    public @NotNull Component staffTicketNote(final @NonNull Ticket ticket, final @NonNull String note, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.add(TagResolver.resolver("note", Tag.preProcessParsed(note)));
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));

        return this.locale.announcement.note.use(templates);
    }

    @Override
    public @NotNull Component staffTicketCreate(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.create.use(templates);
    }

    @Override
    public @NotNull Component staffTicketReopen(final @NonNull Ticket ticket, final @NonNull Soul soul) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));
        templates.addAll(this.templateService.player("player", soul));


        return this.locale.announcement.reopen.use(templates);
    }

    @Override
    public @NotNull Component staffTicketUpdate(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.update.use(templates);
    }

    @Override
    public @NotNull Component staffTicketClose(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.announcement.close.use(templates);
    }

    @Override
    public @NotNull Component commandsTicketList(final @NonNull Collection<@NonNull Ticket> tickets) {
        TagResolver wrapper = TagResolver.resolver("wrapper", Tag.selfClosingInserting(this.locale.title.wrapper.use()));

        TextComponent.Builder builder = Component.text()
                .append(this.locale.title.yourTickets.use(Collections.singletonList(wrapper)));

        List<Ticket> sortedTickets = new ArrayList<>(tickets);
        sortedTickets.sort(Comparator.comparingInt(Ticket::id));

        sortedTickets.forEach(ticket -> {
            List<TagResolver> templates = new ArrayList<>(this.templateService.ticket(ticket));
            templates.add(this.prefix);

            Component list = this.locale.format.list.use(templates);
            builder.append(Component.newline(), list);
        });

        return this.padComponent(builder.build());
    }

    @Override
    public @NotNull Component commandsTicketsList(final @NonNull Map<@NonNull UUID, @NonNull Collection<@NonNull Ticket>> map) {
        TagResolver wrapper = TagResolver.resolver("wrapper", Tag.selfClosingInserting(this.locale.title.wrapper.use()));

        TextComponent.Builder builder = Component.text()
                .append(this.locale.title.allTickets.use(Collections.singletonList(wrapper)));

        map.forEach((uuid, tickets) -> {
            Soul soul = this.userService.wrap(uuid);

            builder.append(Component.newline());
            builder.append(this.locale.format.listHeader.use(this.templateService.player("player", soul)));

            List<Ticket> sortedTickets = new ArrayList<>(tickets);
            sortedTickets.sort(Comparator.comparingInt(Ticket::id));

            for (final Ticket ticket : sortedTickets) {
                List<TagResolver> templates = new ArrayList<>(this.templateService.ticket(ticket));
                templates.add(this.prefix);

                Component list = this.locale.format.list.use(templates);
                builder.append(Component.newline(), list);
            }
        });

        return this.padComponent(builder.build());
    }

    @Override
    public @NotNull Component commandsTeleport(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.addAll(this.templateService.ticket(ticket));

        return this.locale.sender.teleport.use(templates);
    }

    @Override
    public @NotNull Component commandsHighscore(final @NonNull Map<UUID, Integer> ranks) {
        TagResolver wrapper = TagResolver.resolver("wrapper", Tag.selfClosingInserting(this.locale.title.wrapper.use()));

        TextComponent.Builder builder = Component.text()
                .append(this.locale.title.highscores.use(Collections.singletonList(wrapper)), Component.newline());

        List<Component> entries = Lists.map(ranks.entrySet(), (entry) -> {
            Soul soul = this.userService.wrap(entry.getKey());
            List<TagResolver> templates = new ArrayList<>(this.templateService.player("player", soul));
            templates.add(TagResolver.resolver("amount", Tag.preProcessParsed(entry.getValue().toString())));

            return this.locale.format.hs.use(templates);
        });

        return builder.append(Component.join(
                Component.newline(),
                entries
        )).build();
    }

    @Override
    public @NotNull Component commandsLog(final @NonNull Collection<Interaction> interactions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        TagResolver wrapper = TagResolver.resolver("wrapper", Tag.selfClosingInserting(this.locale.title.wrapper.use()));

        TextComponent.Builder builder = Component.text()
                .append(this.locale.title.log.use(Collections.singletonList(wrapper)), Component.newline());

        List<Component> entries = Lists.map(interactions, (interaction) -> {
            Soul soul = this.userService.wrap(interaction.sender());
            List<TagResolver> templates = new ArrayList<>(this.templateService.player("player", soul));
            templates.add(TagResolver.resolver("action", Tag.preProcessParsed(interaction.action().name())));

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
    public @NotNull Component exceptionTicketOpen() {
        return this.locale.exception.ticketOpen.use(Collections.singletonList(this.prefix));
    }

    @Override
    public @NotNull Component exceptionTicketClaimed() {
        return this.locale.exception.ticketClaimed.use(Collections.singletonList(this.prefix));
    }

    @Override
    public @NotNull Component exceptionTicketClosed() {
        return this.locale.exception.ticketClosed.use(Collections.singletonList(this.prefix));
    }

    @Override
    public @NotNull Component exceptionTicketNotFound() {
        return this.locale.exception.ticketNotFound.use(Collections.singletonList(this.prefix));
    }

    @Override
    public @NotNull Component exceptionTooManyTicketsOpen() {
        return this.locale.exception.tooManyTicketsOpen.use(Collections.singletonList(this.prefix));
    }

    @Override
    public @NotNull Component exceptionNoPermission() {
        return this.locale.exception.noPermission.use(Collections.singletonList(this.prefix));
    }

    @Override
    public @NotNull Component exceptionWrongSender(final @NonNull Class<?> sender) {
        TagResolver template = TagResolver.resolver("sender", Tag.preProcessParsed(sender.getSimpleName()));
        return this.locale.exception.invalidSender.use(Arrays.asList(this.prefix, template));
    }

    @Override
    public @NotNull Component showTicket(final @NonNull Ticket ticket) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.add(TagResolver.resolver("wrapper", Tag.selfClosingInserting(this.locale.title.wrapper.use())));
        templates.addAll(this.templateService.ticket(ticket));

        Optional<UUID> claimer = ticket.claimer();
        Component component;

        if (claimer.isPresent()) {
            Soul soul = this.userService.wrap(claimer.get());
            templates.addAll(this.templateService.player("claimer", soul));

            component = this.locale.show.claimed.use(templates);
        } else {
            component = this.locale.show.unclaimed.use(templates);
        }

        return this.padComponent(Component.join(
                Component.newline(),
                this.locale.title.showTicket.use(templates),
                this.locale.show.status.use(templates),
                this.locale.show.player.use(templates),
                this.locale.show.position.use(templates),
                component,
                this.locale.show.message.use(templates)
        ));
    }

    @Override
    public @NotNull Component taskReminder(final int count) {
        List<TagResolver> templates = new ArrayList<>();
        templates.add(this.prefix);
        templates.add(TagResolver.resolver("amount", Tag.preProcessParsed(String.valueOf(count))));

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
