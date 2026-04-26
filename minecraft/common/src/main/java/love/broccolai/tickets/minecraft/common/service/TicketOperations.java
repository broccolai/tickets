package love.broccolai.tickets.minecraft.common.service;

import com.google.inject.Inject;
import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.packaged.TicketAssigned;
import love.broccolai.tickets.api.model.action.packaged.TicketClosed;
import love.broccolai.tickets.api.model.action.packaged.TicketCommented;
import love.broccolai.tickets.api.model.action.packaged.TicketReopened;
import love.broccolai.tickets.api.model.action.packaged.TicketUnassigned;
import love.broccolai.tickets.api.model.format.TicketFormData;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StatisticService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.api.service.TicketSearch;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.model.PlayerCommander;
import love.broccolai.tickets.minecraft.common.utilities.DurationFormatter;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketOperations {

    private final StorageService storage;
    private final ModificationService modifications;
    private final StatisticService statistics;
    private final TicketDisplayService display;
    private final TicketLists lists;
    private final TicketLocations locations;
    private final TicketTeleportService teleport;
    private final MessageService messages;

    @Inject
    public TicketOperations(
        final StorageService storage,
        final ModificationService modifications,
        final StatisticService statistics,
        final TicketDisplayService display,
        final TicketLists lists,
        final TicketLocations locations,
        final TicketTeleportService teleport,
        final MessageService messages
    ) {
        this.storage = storage;
        this.modifications = modifications;
        this.statistics = statistics;
        this.display = display;
        this.lists = lists;
        this.locations = locations;
        this.teleport = teleport;
        this.messages = messages;
    }

    public void create(final PlayerCommander commander, final TicketFormat format, final TicketFormData form) {
        Ticket ticket = this.storage.createTicket(commander.uuid(), format, form);

        commander.sendMessage(this.display.display(ticket));
        this.messages.feedbackUserCreate(commander, ticket);
    }

    public void show(final Commander commander, final Ticket ticket) {
        commander.sendMessage(this.display.display(ticket));
    }

    public void listUser(final PlayerCommander commander, final Set<TicketStatus> statuses) {
        Collection<Ticket> tickets = this.storage.findTickets(
            TicketSearch.matching(statuses).createdBy(commander.uuid())
        );

        commander.sendMessage(this.lists.user(tickets));
    }

    public void listStaff(
        final Commander commander,
        final Set<TicketStatus> statuses,
        final Optional<Profile> target
    ) {
        TicketSearch search = TicketSearch.matching(statuses);

        if (target.isPresent()) {
            search = search.createdBy(target.get().uuid());
        }

        commander.sendMessage(this.lists.staff(this.storage.findTickets(search)));
    }

    public void commentUser(final PlayerCommander commander, final Ticket ticket, final String message) {
        TicketCommented action = this.modifications.comment(ticket, commander.uuid(), message);

        this.messages.feedbackUserComment(commander, ticket.withAction(action));
    }

    public void closeUser(final PlayerCommander commander, final Ticket ticket) {
        TicketClosed action = this.modifications.close(ticket, commander.uuid());

        this.messages.feedbackUserClose(commander, ticket.withAction(action));
    }

    public void claim(final Commander commander, final Ticket ticket) {
        TicketAssigned action = this.modifications.assign(ticket, commander.uuid(), commander.uuid());

        this.messages.feedbackStaffClaim(commander, ticket.withAction(action));
    }

    public void assign(final Commander commander, final Ticket ticket, final UUID targetUser) {
        TicketAssigned action = this.modifications.assign(ticket, commander.uuid(), targetUser);

        this.messages.feedbackStaffAssign(commander, ticket.withAction(action));
    }

    public void unclaim(final Commander commander, final Ticket ticket) {
        TicketUnassigned action = this.modifications.unassign(ticket, commander.uuid());

        this.messages.feedbackStaffUnclaim(commander, ticket.withAction(action));
    }

    public void closeStaff(final Commander commander, final Ticket ticket) {
        TicketClosed action = this.modifications.close(ticket, commander.uuid());

        this.messages.feedbackStaffClose(commander, ticket.withAction(action));
    }

    public void reopen(final Commander commander, final Ticket ticket) {
        TicketReopened action = this.modifications.reopen(ticket, commander.uuid());

        this.messages.feedbackStaffReopen(commander, ticket.withAction(action));
    }

    public void note(final Commander commander, final Ticket ticket, final String message) {
        TicketCommented action = this.modifications.comment(ticket, commander.uuid(), message);

        this.messages.feedbackStaffNote(commander, ticket.withAction(action));
    }

    public void teleport(final PlayerCommander commander, final Ticket ticket) {
        Optional<Location> location = this.locations.find(ticket);

        if (location.isEmpty()) {
            this.messages.feedbackStaffTeleportMissingLocation(commander, ticket);
            return;
        }

        if (this.teleport.teleport(commander, location.get())) {
            this.messages.feedbackStaffTeleport(commander, ticket);
        } else {
            this.messages.feedbackStaffTeleportMissingLocation(commander, ticket);
        }
    }

    public void averageLifespan(final Commander commander, final Duration searchWindow) {
        Duration averageLifespan = this.statistics.averageTicketsLifespan(searchWindow);
        String formattedResult = DurationFormatter.formatDuration(averageLifespan);

        this.messages.feedbackAdminAverageLifespan(commander, formattedResult);
    }
}
